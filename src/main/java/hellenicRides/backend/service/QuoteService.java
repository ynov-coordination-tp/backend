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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuoteService {

  private final QuoteRepository quoteRepository;
  private final QuoteItemRepository quoteItemRepository;
  private final QuoteItemOptionRepository quoteItemOptionRepository;
  private final OptionRepository optionRepository;

  @Transactional
  public Quote createQuote(QuoteCreateDto dto) {
    // Create the quote
    Quote quote =
        Quote.builder()
            .quoteNumber(generateQuoteNumber())
            .customerId(dto.getCustomerId())
            .tourPackageId(dto.getTourPackageId())
            .departureDate(dto.getDepartureDate())
            .returnDate(dto.getReturnDate())
            .participantCount(dto.getItems() != null ? dto.getItems().size() : 0)
            .status("DRAFT")
            .build();

    quote = quoteRepository.save(quote);

    BigDecimal totalPrice = BigDecimal.ZERO;

    // Create quote items
    if (dto.getItems() != null) {
      for (QuoteItemCreateDto itemDto : dto.getItems()) {
        QuoteItem item =
            QuoteItem.builder()
                .quoteId(quote.getId())
                .participantName(itemDto.getParticipantName())
                .motoLocationId(itemDto.getMotoLocationId())
                .accommodationId(itemDto.getAccommodationId())
                .build();

        BigDecimal itemPrice = BigDecimal.ZERO;

        // Create quote item options and calculate price
        if (itemDto.getOptions() != null) {
          for (QuoteItemOptionCreateDto optionDto : itemDto.getOptions()) {
            Optional<Option> optionOpt = optionRepository.findById(optionDto.getOptionId());
            if (optionOpt.isPresent()) {
              Option option = optionOpt.get();
              int quantity = optionDto.getQuantity() != null ? optionDto.getQuantity() : 1;
              BigDecimal optionPrice = option.getPrice().multiply(BigDecimal.valueOf(quantity));
              itemPrice = itemPrice.add(optionPrice);
            }
          }
        }

        item.setLockedUnitPrice(itemPrice);
        item = quoteItemRepository.save(item);

        // Now save the options with the item ID
        if (itemDto.getOptions() != null) {
          for (QuoteItemOptionCreateDto optionDto : itemDto.getOptions()) {
            Optional<Option> optionOpt = optionRepository.findById(optionDto.getOptionId());
            if (optionOpt.isPresent()) {
              Option option = optionOpt.get();
              int quantity = optionDto.getQuantity() != null ? optionDto.getQuantity() : 1;
              BigDecimal optionPrice = option.getPrice().multiply(BigDecimal.valueOf(quantity));

              QuoteItemOption quoteItemOption =
                  QuoteItemOption.builder()
                      .quoteItemId(item.getId())
                      .optionId(option.getId())
                      .quantity(quantity)
                      .lockedPrice(optionPrice)
                      .build();

              quoteItemOptionRepository.save(quoteItemOption);
            }
          }
        }

        totalPrice = totalPrice.add(itemPrice);
      }
    }

    // Update quote with total price
    quote.setLockedTotalPrice(totalPrice);
    return quoteRepository.save(quote);
  }

  public Optional<QuoteResponseDto> getQuoteById(Long id) {
    Optional<Quote> quoteOpt = quoteRepository.findById(id);
    if (quoteOpt.isEmpty()) {
      return Optional.empty();
    }

    Quote quote = quoteOpt.get();
    List<QuoteItem> items = quoteItemRepository.findByQuoteId(id);

    List<QuoteItemResponseDto> itemDtos =
        items.stream()
            .map(
                item -> {
                  List<QuoteItemOption> options =
                      quoteItemOptionRepository.findByQuoteItemId(item.getId());
                  List<QuoteItemOptionResponseDto> optionDtos =
                      options.stream()
                          .map(QuoteItemOptionResponseDto::fromEntity)
                          .collect(Collectors.toList());
                  return QuoteItemResponseDto.fromEntity(item, optionDtos);
                })
            .collect(Collectors.toList());

    return Optional.of(QuoteResponseDto.fromEntity(quote, itemDtos));
  }

  @Transactional
  public Optional<Quote> updateQuote(Long id, QuoteCreateDto dto) {
    Optional<Quote> existingQuoteOpt = quoteRepository.findById(id);
    if (existingQuoteOpt.isEmpty()) {
      return Optional.empty();
    }

    // Delete existing items and options
    List<QuoteItem> existingItems = quoteItemRepository.findByQuoteId(id);
    for (QuoteItem item : existingItems) {
      quoteItemOptionRepository.deleteByQuoteItemId(item.getId());
    }
    quoteItemRepository.deleteByQuoteId(id);

    // Update quote
    Quote quote = existingQuoteOpt.get();
    quote.setCustomerId(dto.getCustomerId());
    quote.setTourPackageId(dto.getTourPackageId());
    quote.setDepartureDate(dto.getDepartureDate());
    quote.setReturnDate(dto.getReturnDate());
    quote.setParticipantCount(dto.getItems() != null ? dto.getItems().size() : 0);

    BigDecimal totalPrice = BigDecimal.ZERO;

    // Create new quote items
    if (dto.getItems() != null) {
      for (QuoteItemCreateDto itemDto : dto.getItems()) {
        QuoteItem item =
            QuoteItem.builder()
                .quoteId(quote.getId())
                .participantName(itemDto.getParticipantName())
                .motoLocationId(itemDto.getMotoLocationId())
                .accommodationId(itemDto.getAccommodationId())
                .build();

        BigDecimal itemPrice = BigDecimal.ZERO;

        item = quoteItemRepository.save(item);

        if (itemDto.getOptions() != null) {
          for (QuoteItemOptionCreateDto optionDto : itemDto.getOptions()) {
            Optional<Option> optionOpt = optionRepository.findById(optionDto.getOptionId());
            if (optionOpt.isPresent()) {
              Option option = optionOpt.get();
              int quantity = optionDto.getQuantity() != null ? optionDto.getQuantity() : 1;
              BigDecimal optionPrice = option.getPrice().multiply(BigDecimal.valueOf(quantity));

              QuoteItemOption quoteItemOption =
                  QuoteItemOption.builder()
                      .quoteItemId(item.getId())
                      .optionId(option.getId())
                      .quantity(quantity)
                      .lockedPrice(optionPrice)
                      .build();

              quoteItemOptionRepository.save(quoteItemOption);
              itemPrice = itemPrice.add(optionPrice);
            }
          }
        }

        item.setLockedUnitPrice(itemPrice);
        quoteItemRepository.save(item);
        totalPrice = totalPrice.add(itemPrice);
      }
    }

    quote.setLockedTotalPrice(totalPrice);
    return Optional.of(quoteRepository.save(quote));
  }

  public List<Quote> getQuotesByCustomerId(Long customerId) {
    return quoteRepository.findByCustomerId(customerId);
  }

  private String generateQuoteNumber() {
    return "QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }
}
