package hellenicRides.backend.service;

import hellenicRides.backend.entity.Accommodation;
import hellenicRides.backend.entity.AccommodationPrice;
import hellenicRides.backend.repository.AccommodationPriceRepository;
import hellenicRides.backend.repository.AccommodationRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationService {

  private final AccommodationRepository accommodationRepository;
  private final AccommodationPriceRepository accommodationPriceRepository;

  public List<Accommodation> getAllAccommodations() {
    return accommodationRepository.findAll();
  }

  public Optional<Accommodation> getAccommodationById(Long id) {
    return accommodationRepository.findById(id);
  }

  public List<Accommodation> getAccommodationsByCountry(String country) {
    return accommodationRepository.findByCountry(country);
  }

  public Optional<AccommodationPrice> getAccommodationPrice(
      Long accommodationId, LocalDate date, String roomType) {
    return accommodationPriceRepository.findPriceForDate(accommodationId, roomType, date);
  }

  public BigDecimal calculateAccommodationCost(
      Long accommodationId, LocalDate startDate, LocalDate endDate, String roomType) {
    BigDecimal totalCost = BigDecimal.ZERO;
    LocalDate currentDate = startDate;

    while (!currentDate.isAfter(endDate)) {
      Optional<AccommodationPrice> priceOpt =
          getAccommodationPrice(accommodationId, currentDate, roomType);
      if (priceOpt.isPresent()) {
        totalCost = totalCost.add(priceOpt.get().getNightlyPrice());
      }
      currentDate = currentDate.plusDays(1);
    }

    return totalCost;
  }
}
