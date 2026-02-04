package hellenicRides.backend.service;

import hellenicRides.backend.dto.*;
import hellenicRides.backend.entity.*;
import hellenicRides.backend.repository.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des devis. Responsabilité : Orchestrer la création, mise à jour et
 * récupération des devis.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuoteService {

  private final QuoteRepository quoteRepository;
  private final QuoteItemRepository quoteItemRepository;
  private final QuoteItemOptionRepository quoteItemOptionRepository;
  private final OptionRepository optionRepository;
  private final PricingService pricingService;

  // ========== API PUBLIQUE ==========

  /**
   * Crée un nouveau devis avec tous ses items et options.
   *
   * @param dto Données du devis à créer
   * @return Le devis créé avec son prix total calculé
   */
  @Transactional
  public Quote createQuote(QuoteCreateDto dto) {
    logQuoteCreationStart(dto);

    validateQuoteCreationRequest(dto);

    Quote quote = buildAndSaveQuote(dto);
    BigDecimal totalPrice = processQuoteItems(quote, dto);

    return finalizeQuote(quote, totalPrice);
  }

  /**
   * Récupère un devis par son ID avec tous ses détails.
   *
   * @param id ID du devis
   * @return Le devis avec tous ses items et options
   */
  public Optional<QuoteResponseDto> getQuoteById(Long id) {
    Optional<Quote> quoteOpt = quoteRepository.findById(id);

    if (quoteOpt.isEmpty()) {
      log.debug("Quote not found with id: {}", id);
      return Optional.empty();
    }

    Quote quote = quoteOpt.get();
    List<QuoteItemResponseDto> itemDtos = buildQuoteItemResponses(id);

    return Optional.of(QuoteResponseDto.fromEntity(quote, itemDtos));
  }

  /**
   * Met à jour un devis existant.
   *
   * @param id ID du devis à mettre à jour
   * @param dto Nouvelles données du devis
   * @return Le devis mis à jour
   */
  @Transactional
  public Optional<Quote> updateQuote(Long id, QuoteCreateDto dto) {
    Optional<Quote> existingQuoteOpt = quoteRepository.findById(id);

    if (existingQuoteOpt.isEmpty()) {
      log.warn("Cannot update quote: Quote not found with id {}", id);
      return Optional.empty();
    }

    validateQuoteCreationRequest(dto);

    Quote quote = existingQuoteOpt.get();
    deleteExistingQuoteItems(id);
    updateQuoteBasicInfo(quote, dto);
    BigDecimal totalPrice = processQuoteItems(quote, dto);

    return Optional.of(finalizeQuote(quote, totalPrice));
  }

  /**
   * Récupère tous les devis d'un client.
   *
   * @param customerId ID du client
   * @return Liste des devis du client
   */
  public List<Quote> getQuotesByCustomerId(Long customerId) {
    log.debug("Fetching quotes for customer: {}", customerId);
    return quoteRepository.findByCustomerId(customerId);
  }

  // ========== VALIDATION ==========

  private void validateQuoteCreationRequest(QuoteCreateDto dto) {
    validateFormulaIdPresent(dto);
    validateFormulaAllowedForTour(dto);
  }

  private void validateFormulaIdPresent(QuoteCreateDto dto) {
    if (dto.getFormulaId() == null) {
      throw new IllegalArgumentException("Formula ID is required");
    }
  }

  private void validateFormulaAllowedForTour(QuoteCreateDto dto) {
    if (!pricingService.isFormulaAllowedForTour(dto.getTourPackageId(), dto.getFormulaId())) {
      throw new IllegalArgumentException(
          String.format(
              "Formula %d is not allowed for tour %d", dto.getFormulaId(), dto.getTourPackageId()));
    }
  }

  // ========== CRÉATION QUOTE ==========

  private Quote buildAndSaveQuote(QuoteCreateDto dto) {
    Quote quote = buildQuote(dto);
    return quoteRepository.save(quote);
  }

  private Quote buildQuote(QuoteCreateDto dto) {
    return Quote.builder()
        .quoteNumber(generateQuoteNumber())
        .customerId(dto.getCustomerId())
        .tourPackageId(dto.getTourPackageId())
        .departureDate(dto.getDepartureDate())
        .returnDate(dto.getReturnDate())
        .participantCount(calculateParticipantCount(dto))
        .status("DRAFT")
        .build();
  }

  private int calculateParticipantCount(QuoteCreateDto dto) {
    return dto.getItems() != null ? dto.getItems().size() : 0;
  }

  private String generateQuoteNumber() {
    return "QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  // ========== TRAITEMENT ITEMS ==========

  private BigDecimal processQuoteItems(Quote quote, QuoteCreateDto dto) {
    if (dto.getItems() == null || dto.getItems().isEmpty()) {
      log.warn("No items provided for quote {}", quote.getId());
      return BigDecimal.ZERO;
    }

    BigDecimal totalPrice = BigDecimal.ZERO;

    for (QuoteItemCreateDto itemDto : dto.getItems()) {
      BigDecimal itemTotalPrice = processQuoteItem(quote, dto, itemDto);
      totalPrice = totalPrice.add(itemTotalPrice);
    }

    return totalPrice;
  }

  private BigDecimal processQuoteItem(Quote quote, QuoteCreateDto dto, QuoteItemCreateDto itemDto) {
    logItemProcessingStart(itemDto);

    BigDecimal basePrice = calculateItemBasePrice(dto, itemDto);
    BigDecimal optionsPrice = calculateItemOptionsPrice(itemDto);
    BigDecimal totalItemPrice = basePrice.add(optionsPrice);

    QuoteItem savedItem = saveQuoteItem(quote, itemDto, totalItemPrice);
    saveQuoteItemOptions(savedItem, itemDto);

    logItemProcessingEnd(itemDto, totalItemPrice);

    return totalItemPrice;
  }

  // ========== CALCUL PRIX ITEM ==========

  private BigDecimal calculateItemBasePrice(QuoteCreateDto dto, QuoteItemCreateDto itemDto) {
    BigDecimal basePrice =
        pricingService.calculateQuoteItemPrice(
            dto.getTourPackageId(),
            dto.getFormulaId(),
            itemDto.getMotoLocationId(),
            itemDto.getAccommodationId(),
            getRoomType(itemDto),
            dto.getDepartureDate(),
            dto.getReturnDate());

    log.info("Base item price (tour+moto+accommodation): {}", basePrice);
    return basePrice;
  }

  private String getRoomType(QuoteItemCreateDto itemDto) {
    return itemDto.getRoomType() != null ? itemDto.getRoomType() : "SINGLE";
  }

  // ========== CALCUL PRIX OPTIONS ==========

  private BigDecimal calculateItemOptionsPrice(QuoteItemCreateDto itemDto) {
    if (itemDto.getOptions() == null || itemDto.getOptions().isEmpty()) {
      return BigDecimal.ZERO;
    }

    BigDecimal totalOptionsPrice = BigDecimal.ZERO;

    for (QuoteItemOptionCreateDto optionDto : itemDto.getOptions()) {
      BigDecimal optionPrice = calculateSingleOptionPrice(optionDto);
      totalOptionsPrice = totalOptionsPrice.add(optionPrice);
    }

    return totalOptionsPrice;
  }

  private BigDecimal calculateSingleOptionPrice(QuoteItemOptionCreateDto optionDto) {
    Optional<Option> optionOpt = optionRepository.findById(optionDto.getOptionId());

    if (optionOpt.isEmpty()) {
      log.warn("Option not found with id: {}", optionDto.getOptionId());
      return BigDecimal.ZERO;
    }

    Option option = optionOpt.get();
    int quantity = getOptionQuantity(optionDto);
    BigDecimal optionPrice = option.getPrice().multiply(BigDecimal.valueOf(quantity));

    log.debug("Adding option: {} x {} = {}", option.getName(), quantity, optionPrice);

    return optionPrice;
  }

  private int getOptionQuantity(QuoteItemOptionCreateDto optionDto) {
    return optionDto.getQuantity() != null ? optionDto.getQuantity() : 1;
  }

  // ========== SAUVEGARDE ITEM ==========

  private QuoteItem saveQuoteItem(Quote quote, QuoteItemCreateDto itemDto, BigDecimal totalPrice) {
    QuoteItem item = buildQuoteItem(quote, itemDto, totalPrice);
    return quoteItemRepository.save(item);
  }

  private QuoteItem buildQuoteItem(Quote quote, QuoteItemCreateDto itemDto, BigDecimal totalPrice) {
    return QuoteItem.builder()
        .quoteId(quote.getId())
        .participantName(itemDto.getParticipantName())
        .motoLocationId(itemDto.getMotoLocationId())
        .accommodationId(itemDto.getAccommodationId())
        .lockedUnitPrice(totalPrice)
        .build();
  }

  // ========== SAUVEGARDE OPTIONS ==========

  private void saveQuoteItemOptions(QuoteItem item, QuoteItemCreateDto itemDto) {
    if (itemDto.getOptions() == null || itemDto.getOptions().isEmpty()) {
      return;
    }

    for (QuoteItemOptionCreateDto optionDto : itemDto.getOptions()) {
      saveQuoteItemOption(item, optionDto);
    }
  }

  private void saveQuoteItemOption(QuoteItem item, QuoteItemOptionCreateDto optionDto) {
    Optional<Option> optionOpt = optionRepository.findById(optionDto.getOptionId());

    if (optionOpt.isEmpty()) {
      log.warn("Skipping option {} - not found", optionDto.getOptionId());
      return;
    }

    Option option = optionOpt.get();
    int quantity = getOptionQuantity(optionDto);
    BigDecimal optionPrice = option.getPrice().multiply(BigDecimal.valueOf(quantity));

    QuoteItemOption quoteItemOption = buildQuoteItemOption(item, option, quantity, optionPrice);
    quoteItemOptionRepository.save(quoteItemOption);
  }

  private QuoteItemOption buildQuoteItemOption(
      QuoteItem item, Option option, int quantity, BigDecimal price) {
    return QuoteItemOption.builder()
        .quoteItemId(item.getId())
        .optionId(option.getId())
        .quantity(quantity)
        .lockedPrice(price)
        .build();
  }

  // ========== FINALISATION QUOTE ==========

  private Quote finalizeQuote(Quote quote, BigDecimal totalPrice) {
    quote.setLockedTotalPrice(totalPrice);
    Quote savedQuote = quoteRepository.save(quote);

    logQuoteCreationEnd(savedQuote, totalPrice);

    return savedQuote;
  }

  // ========== MISE À JOUR QUOTE ==========

  private void deleteExistingQuoteItems(Long quoteId) {
    List<QuoteItem> existingItems = quoteItemRepository.findByQuoteId(quoteId);

    for (QuoteItem item : existingItems) {
      quoteItemOptionRepository.deleteByQuoteItemId(item.getId());
    }

    quoteItemRepository.deleteByQuoteId(quoteId);
    log.debug("Deleted existing items for quote {}", quoteId);
  }

  private void updateQuoteBasicInfo(Quote quote, QuoteCreateDto dto) {
    quote.setCustomerId(dto.getCustomerId());
    quote.setTourPackageId(dto.getTourPackageId());
    quote.setDepartureDate(dto.getDepartureDate());
    quote.setReturnDate(dto.getReturnDate());
    quote.setParticipantCount(calculateParticipantCount(dto));
  }

  // ========== CONSTRUCTION RÉPONSE ==========

  private List<QuoteItemResponseDto> buildQuoteItemResponses(Long quoteId) {
    List<QuoteItem> items = quoteItemRepository.findByQuoteId(quoteId);

    return items.stream().map(this::buildQuoteItemResponse).collect(Collectors.toList());
  }

  private QuoteItemResponseDto buildQuoteItemResponse(QuoteItem item) {
    List<QuoteItemOption> options = quoteItemOptionRepository.findByQuoteItemId(item.getId());

    List<QuoteItemOptionResponseDto> optionDtos =
        options.stream().map(QuoteItemOptionResponseDto::fromEntity).collect(Collectors.toList());

    return QuoteItemResponseDto.fromEntity(item, optionDtos);
  }

  // ========== LOGGING ==========

  private void logQuoteCreationStart(QuoteCreateDto dto) {
    log.info("Creating quote for tour={}, formula={}", dto.getTourPackageId(), dto.getFormulaId());
  }

  private void logQuoteCreationEnd(Quote quote, BigDecimal totalPrice) {
    log.info(
        "Quote created successfully: {} with total price: {} centimes",
        quote.getQuoteNumber(),
        totalPrice);
  }

  private void logItemProcessingStart(QuoteItemCreateDto itemDto) {
    log.info("Processing item for participant: {}", itemDto.getParticipantName());
  }

  private void logItemProcessingEnd(QuoteItemCreateDto itemDto, BigDecimal totalPrice) {
    log.info("Item processed for {}: {} centimes", itemDto.getParticipantName(), totalPrice);
  }
}
