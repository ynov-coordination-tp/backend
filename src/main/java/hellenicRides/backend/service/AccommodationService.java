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

  public Accommodation saveAccommodation(Accommodation accommodation) {
    return accommodationRepository.save(accommodation);
  }

  public Optional<Accommodation> updateAccommodation(Long id, Accommodation accommodationDetails) {
    return accommodationRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(accommodationDetails.getName());
              existing.setCity(accommodationDetails.getCity());
              existing.setCountry(accommodationDetails.getCountry());
              existing.setType(accommodationDetails.getType());
              return accommodationRepository.save(existing);
            });
  }

  public void deleteAccommodation(Long id) {
    accommodationRepository.deleteById(id);
  }

  public List<AccommodationPrice> getAllAccommodationPrices() {
    return accommodationPriceRepository.findAll();
  }

  public Optional<AccommodationPrice> getAccommodationPriceById(Long id) {
    return accommodationPriceRepository.findById(id);
  }

  public List<AccommodationPrice> getAccommodationPricesByAccommodationId(Long accommodationId) {
    return accommodationPriceRepository.findByAccommodationId(accommodationId);
  }

  public AccommodationPrice saveAccommodationPrice(AccommodationPrice price) {
    return accommodationPriceRepository.save(price);
  }

  public Optional<AccommodationPrice> updateAccommodationPrice(
      Long id, AccommodationPrice details) {
    return accommodationPriceRepository
        .findById(id)
        .map(
            existing -> {
              existing.setAccommodationId(details.getAccommodationId());
              existing.setStartDate(details.getStartDate());
              existing.setEndDate(details.getEndDate());
              existing.setNightlyPrice(details.getNightlyPrice());
              existing.setRoomType(details.getRoomType());
              return accommodationPriceRepository.save(existing);
            });
  }

  public boolean deleteAccommodationPrice(Long id) {
    if (!accommodationPriceRepository.existsById(id)) {
      return false;
    }
    accommodationPriceRepository.deleteById(id);
    return true;
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
