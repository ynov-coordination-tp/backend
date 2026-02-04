package hellenicRides.backend.service;

import hellenicRides.backend.entity.*;
import hellenicRides.backend.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service de calcul des prix selon les règles métier du cahier des charges. Tous les prix sont en
 * CENTIMES pour éviter les erreurs d'arrondi.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PricingService {

  private static final BigDecimal CENTS_TO_EUROS = new BigDecimal("100");
  private static final int ESTIMATED_KM_PER_DAY = 200;

  private final TourPriceRepository tourPriceRepository;
  private final MotoCategoryPriceRepository motoCategoryPriceRepository;
  private final AccommodationPriceRepository accommodationPriceRepository;
  private final MotoLocationRepository motoLocationRepository;
  private final TourFormulaRepository tourFormulaRepository;
  private final FormulaRepository formulaRepository;
  private final TourRepository tourRepository;

  /**
   * Point d'entrée principal : calcule le prix total d'un QuoteItem.
   *
   * @return Prix en CENTIMES
   */
  public BigDecimal calculateQuoteItemPrice(
      Long tourId,
      Long formulaId,
      Long motoLocationId,
      Long accommodationId,
      String roomType,
      LocalDate departureDate,
      LocalDate returnDate) {

    logCalculationStart(tourId, formulaId, departureDate, returnDate);

    Formula formula = getAndValidateFormula(tourId, formulaId);
    TourFormula tourFormula = getTourFormula(tourId, formulaId);

    BigDecimal totalPrice = BigDecimal.ZERO;

    totalPrice = addTourBasePrice(totalPrice, tourFormula, departureDate);
    totalPrice =
        addMotoPrice(totalPrice, formula, motoLocationId, departureDate, returnDate, tourId);
    totalPrice =
        addAccommodationPrice(
            totalPrice, formula, accommodationId, roomType, departureDate, returnDate);

    logCalculationEnd(totalPrice);
    return totalPrice;
  }

  // ========== VALIDATION & RÉCUPÉRATION ==========

  private Formula getAndValidateFormula(Long tourId, Long formulaId) {
    Formula formula = findFormula(formulaId);
    validateFormulaForTour(tourId, formulaId, formula);
    return formula;
  }

  private Formula findFormula(Long formulaId) {
    return formulaRepository
        .findById(formulaId)
        .orElseThrow(() -> new IllegalArgumentException("Formula not found: " + formulaId));
  }

  private void validateFormulaForTour(Long tourId, Long formulaId, Formula formula) {
    if (!isFormulaAllowedForTour(tourId, formulaId)) {
      throw new IllegalArgumentException(
          String.format("Formula %s is not allowed for tour %d", formula.getName(), tourId));
    }
  }

  private TourFormula getTourFormula(Long tourId, Long formulaId) {
    List<TourFormula> tourFormulas = tourFormulaRepository.findByTourIdAndIsActiveTrue(tourId);
    return tourFormulas.stream()
        .filter(tf -> tf.getFormula().getId().equals(formulaId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("TourFormula not found"));
  }

  // ========== CALCUL PRIX TOUR ==========

  private BigDecimal addTourBasePrice(
      BigDecimal currentTotal, TourFormula tourFormula, LocalDate departureDate) {

    BigDecimal tourPrice = calculateTourBasePrice(tourFormula.getId(), departureDate);
    logTourPrice(tourPrice);
    return currentTotal.add(tourPrice);
  }

  private BigDecimal calculateTourBasePrice(Long tourFormulaId, LocalDate date) {
    List<TourPrice> prices = findTourPricesForDate(tourFormulaId, date);

    if (prices.isEmpty()) {
      throw new IllegalArgumentException(
          String.format(
              "No tour base price found for tour formula %d on date %s", tourFormulaId, date));
    }

    BigDecimal basePrice = prices.get(0).getBasePrice();
    log.debug("Tour base price for formula {} on {}: {} centimes", tourFormulaId, date, basePrice);
    return basePrice;
  }

  private List<TourPrice> findTourPricesForDate(Long tourFormulaId, LocalDate date) {
    return tourPriceRepository
        .findByTourFormulaIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            tourFormulaId, date, date);
  }

  // ========== CALCUL PRIX MOTO ==========

  private BigDecimal addMotoPrice(
      BigDecimal currentTotal,
      Formula formula,
      Long motoLocationId,
      LocalDate departureDate,
      LocalDate returnDate,
      Long tourId) {

    if (shouldSkipMotoCalculation(formula, motoLocationId)) {
      log.info("No moto selected (Poseidon allows this)");
      return currentTotal;
    }

    validateMotoRequired(formula, motoLocationId);

    BigDecimal motoPrice = calculateMotoPrice(motoLocationId, departureDate, returnDate, tourId);
    logMotoPrice(formula, motoPrice);
    return currentTotal.add(motoPrice);
  }

  private boolean shouldSkipMotoCalculation(Formula formula, Long motoLocationId) {
    return !Boolean.TRUE.equals(formula.getIncludesMoto()) && motoLocationId == null;
  }

  private void validateMotoRequired(Formula formula, Long motoLocationId) {
    if (Boolean.TRUE.equals(formula.getIncludesMoto()) && motoLocationId == null) {
      throw new IllegalArgumentException(
          String.format("Moto is mandatory for formula %s", formula.getName()));
    }
  }

  private BigDecimal calculateMotoPrice(
      Long motoLocationId, LocalDate departureDate, LocalDate returnDate, Long tourId) {

    MotoLocation motoLocation = findMotoLocation(motoLocationId);
    Tour tour = findTour(tourId);
    MotoCategoryPrice validPrice = findValidMotoPrice(motoLocation, tour, departureDate);

    long days = calculateDays(departureDate, returnDate);

    BigDecimal totalPrice = BigDecimal.ZERO;
    totalPrice = addDailyMotoPrice(totalPrice, validPrice, days);
    totalPrice = addKilometerMotoPrice(totalPrice, validPrice, tour, tourId);

    validateMotoPriceNotZero(totalPrice, motoLocation);
    return totalPrice;
  }

  private MotoLocation findMotoLocation(Long motoLocationId) {
    return motoLocationRepository
        .findById(motoLocationId)
        .orElseThrow(
            () -> new IllegalArgumentException("MotoLocation not found: " + motoLocationId));
  }

  private Tour findTour(Long tourId) {
    return tourRepository
        .findById(tourId)
        .orElseThrow(() -> new IllegalArgumentException("Tour not found: " + tourId));
  }

  private MotoCategoryPrice findValidMotoPrice(
      MotoLocation motoLocation, Tour tour, LocalDate departureDate) {

    List<MotoCategoryPrice> prices =
        motoCategoryPriceRepository.findByMotoCategoryIdAndCountry(
            motoLocation.getMotoCategory().getId(), tour.getCountry());

    return prices.stream()
        .filter(p -> isDateInPeriod(departureDate, p.getStartDate(), p.getEndDate()))
        .findFirst()
        .orElseThrow(() -> createMotoPriceNotFoundException(motoLocation, tour, departureDate));
  }

  private boolean isDateInPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }

  private IllegalArgumentException createMotoPriceNotFoundException(
      MotoLocation motoLocation, Tour tour, LocalDate date) {
    return new IllegalArgumentException(
        String.format(
            "No moto price found for category %s in %s for date %s",
            motoLocation.getMotoCategory().getName(), tour.getCountry(), date));
  }

  private long calculateDays(LocalDate startDate, LocalDate endDate) {
    return ChronoUnit.DAYS.between(startDate, endDate) + 1;
  }

  private BigDecimal addDailyMotoPrice(
      BigDecimal currentTotal, MotoCategoryPrice validPrice, long days) {

    if (!hasDailyPrice(validPrice)) {
      return currentTotal;
    }

    BigDecimal dailyPrice = validPrice.getDailyPrice();
    BigDecimal totalDailyPrice = dailyPrice.multiply(BigDecimal.valueOf(days));

    log.debug(
        "Moto price by day: {} days x {} centimes/day = {} centimes",
        days,
        dailyPrice,
        totalDailyPrice);

    return currentTotal.add(totalDailyPrice);
  }

  private boolean hasDailyPrice(MotoCategoryPrice price) {
    return price.getDailyPrice() != null && price.getDailyPrice().compareTo(BigDecimal.ZERO) > 0;
  }

  private BigDecimal addKilometerMotoPrice(
      BigDecimal currentTotal, MotoCategoryPrice validPrice, Tour tour, Long tourId) {

    if (!hasKilometerPrice(validPrice)) {
      return currentTotal;
    }

    Integer distance = getTourDistance(tour);
    if (distance == null || distance <= 0) {
      log.warn("KmPrice is defined but tour distance is not available for tour {}", tourId);
      return currentTotal;
    }

    BigDecimal kmPrice = validPrice.getKmPrice();
    BigDecimal totalKmPrice = kmPrice.multiply(BigDecimal.valueOf(distance));

    log.debug(
        "Moto price by km: {} km x {} centimes/km = {} centimes", distance, kmPrice, totalKmPrice);

    return currentTotal.add(totalKmPrice);
  }

  private boolean hasKilometerPrice(MotoCategoryPrice price) {
    return price.getKmPrice() != null && price.getKmPrice().compareTo(BigDecimal.ZERO) > 0;
  }

  private Integer getTourDistance(Tour tour) {
    if (hasExplicitDistance(tour)) {
      return tour.getDistanceKm();
    }

    return estimateDistanceFromDuration(tour);
  }

  private boolean hasExplicitDistance(Tour tour) {
    return tour.getDistanceKm() != null && tour.getDistanceKm() > 0;
  }

  private Integer estimateDistanceFromDuration(Tour tour) {
    if (tour.getDurationDays() == null) {
      log.warn("Distance not available for tour {}, km pricing cannot be applied", tour.getId());
      return null;
    }

    int estimatedDistance = tour.getDurationDays() * ESTIMATED_KM_PER_DAY;
    log.warn(
        "Using estimated distance for tour {} ({} km based on {} days)",
        tour.getId(),
        estimatedDistance,
        tour.getDurationDays());
    return estimatedDistance;
  }

  private void validateMotoPriceNotZero(BigDecimal price, MotoLocation motoLocation) {
    if (price.compareTo(BigDecimal.ZERO) == 0) {
      throw new IllegalArgumentException(
          String.format(
              "No valid pricing found (neither daily nor km price) for moto category %s",
              motoLocation.getMotoCategory().getName()));
    }
  }

  // ========== CALCUL PRIX HÉBERGEMENT ==========

  private BigDecimal addAccommodationPrice(
      BigDecimal currentTotal,
      Formula formula,
      Long accommodationId,
      String roomType,
      LocalDate departureDate,
      LocalDate returnDate) {

    if (shouldSkipAccommodation(formula, accommodationId)) {
      return currentTotal;
    }

    validateAccommodationRequired(formula, accommodationId);

    BigDecimal accommodationPrice =
        calculateAccommodationPrice(accommodationId, roomType, departureDate, returnDate);
    logAccommodationPrice(accommodationPrice);
    return currentTotal.add(accommodationPrice);
  }

  private boolean shouldSkipAccommodation(Formula formula, Long accommodationId) {
    if (!Boolean.TRUE.equals(formula.getIncludesAccommodation())) {
      if (accommodationId != null) {
        log.warn("Accommodation provided for {} formula but will be ignored", formula.getName());
      }
      return true;
    }
    return false;
  }

  private void validateAccommodationRequired(Formula formula, Long accommodationId) {
    if (Boolean.TRUE.equals(formula.getIncludesAccommodation()) && accommodationId == null) {
      throw new IllegalArgumentException(
          String.format("Accommodation is mandatory for formula %s", formula.getName()));
    }
  }

  private BigDecimal calculateAccommodationPrice(
      Long accommodationId, String roomType, LocalDate departureDate, LocalDate returnDate) {

    BigDecimal totalPrice = BigDecimal.ZERO;
    LocalDate currentDate = departureDate;

    while (currentDate.isBefore(returnDate)) {
      BigDecimal nightlyPrice = getNightlyPrice(accommodationId, roomType, currentDate);
      totalPrice = totalPrice.add(nightlyPrice);

      log.debug("Accommodation night {}: {} centimes", currentDate, nightlyPrice);
      currentDate = currentDate.plusDays(1);
    }

    log.debug("Total accommodation price: {} centimes", totalPrice);
    return totalPrice;
  }

  private BigDecimal getNightlyPrice(Long accommodationId, String roomType, LocalDate date) {
    Optional<AccommodationPrice> priceOpt =
        accommodationPriceRepository.findPriceForDate(accommodationId, roomType, date);

    return priceOpt
        .map(AccommodationPrice::getNightlyPrice)
        .orElseThrow(
            () -> createAccommodationPriceNotFoundException(accommodationId, roomType, date));
  }

  private IllegalArgumentException createAccommodationPriceNotFoundException(
      Long accommodationId, String roomType, LocalDate date) {
    return new IllegalArgumentException(
        String.format(
            "No accommodation price found for accommodation %d, room type %s, date %s",
            accommodationId, roomType, date));
  }

  // ========== MÉTHODES PUBLIQUES ==========

  /** Valide qu'une formule est autorisée pour un tour donné. */
  public boolean isFormulaAllowedForTour(Long tourId, Long formulaId) {
    List<TourFormula> tourFormulas = tourFormulaRepository.findByTourIdAndIsActiveTrue(tourId);
    return tourFormulas.stream().anyMatch(tf -> tf.getFormula().getId().equals(formulaId));
  }

  /** Récupère les formules actives pour un tour. */
  public List<TourFormula> getActiveFormulasForTour(Long tourId) {
    return tourFormulaRepository.findByTourIdAndIsActiveTrue(tourId);
  }

  // ========== LOGGING ==========

  private void logCalculationStart(
      Long tourId, Long formulaId, LocalDate departureDate, LocalDate returnDate) {
    log.info(
        "Calculating price for tour={}, formula={}, dates={} to {}",
        tourId,
        formulaId,
        departureDate,
        returnDate);
  }

  private void logTourPrice(BigDecimal price) {
    log.info("Tour base price: {} centimes ({}€)", price, toEuros(price));
  }

  private void logMotoPrice(Formula formula, BigDecimal price) {
    String mandatory = Boolean.TRUE.equals(formula.getIncludesMoto()) ? "mandatory" : "optional";
    log.info("Moto price ({}): {} centimes ({}€)", mandatory, price, toEuros(price));
  }

  private void logAccommodationPrice(BigDecimal price) {
    log.info("Accommodation price: {} centimes ({}€)", price, toEuros(price));
  }

  private void logCalculationEnd(BigDecimal totalPrice) {
    log.info("Total calculated price: {} centimes ({}€)", totalPrice, toEuros(totalPrice));
  }

  private BigDecimal toEuros(BigDecimal centimes) {
    return centimes.divide(CENTS_TO_EUROS, 2, RoundingMode.HALF_UP);
  }
}
