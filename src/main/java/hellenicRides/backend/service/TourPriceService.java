package hellenicRides.backend.service;

import hellenicRides.backend.entity.TourPrice;
import hellenicRides.backend.repository.TourPriceRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourPriceService {

  private final TourPriceRepository tourPriceRepository;

  public List<TourPrice> getPricesForFormula(Long tourFormulaId) {
    log.info("Fetching prices for tour formula id: {}", tourFormulaId);
    return tourPriceRepository.findByTourFormulaId(tourFormulaId);
  }

  public List<TourPrice> getPricesForFormulaAndDate(Long tourFormulaId, LocalDate date) {
    // Find prices where startDate <= date AND endDate >= date
    log.info("Fetching prices for tour formula id: {} on date: {}", tourFormulaId, date);
    return tourPriceRepository
        .findByTourFormulaIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            tourFormulaId, date, date);
  }

  public TourPrice saveTourPrice(TourPrice tourPrice) {
    log.info("Saving tour price");
    return tourPriceRepository.save(tourPrice);
  }
}
