package hellenicRides.backend.service;

import hellenicRides.backend.entity.MotoCategory;
import hellenicRides.backend.entity.MotoCategoryPrice;
import hellenicRides.backend.entity.MotoLocation;
import hellenicRides.backend.repository.MotoCategoryPriceRepository;
import hellenicRides.backend.repository.MotoCategoryRepository;
import hellenicRides.backend.repository.MotoLocationRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MotoService {

  private final MotoCategoryRepository motoCategoryRepository;
  private final MotoCategoryPriceRepository motoCategoryPriceRepository;
  private final MotoLocationRepository motoLocationRepository;

  // --- Categories ---
  public List<MotoCategory> getAllMotoCategories() {
    log.info("Fetching all moto categories");
    return motoCategoryRepository.findAll();
  }

  public Optional<MotoCategory> getMotoCategoryById(Long id) {
    log.info("Fetching moto category with id: {}", id);
    return motoCategoryRepository.findById(id);
  }

  public MotoCategory saveMotoCategory(MotoCategory category) {
    return motoCategoryRepository.save(category);
  }

  public Optional<MotoCategory> updateMotoCategory(Long id, MotoCategory details) {
    return motoCategoryRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(details.getName());
              return motoCategoryRepository.save(existing);
            });
  }

  public boolean deleteMotoCategory(Long id) {
    if (!motoCategoryRepository.existsById(id)) {
      return false;
    }
    motoCategoryRepository.deleteById(id);
    return true;
  }

  // --- Prices ---
  public List<MotoCategoryPrice> getAllMotoCategoryPrices() {
    return motoCategoryPriceRepository.findAll();
  }

  public Optional<MotoCategoryPrice> getMotoCategoryPriceById(Long id) {
    return motoCategoryPriceRepository.findById(id);
  }

  public List<MotoCategoryPrice> getPricesForCategory(Long categoryId) {
    log.info("Fetching prices for moto category id: {}", categoryId);
    return motoCategoryPriceRepository.findByMotoCategoryId(categoryId);
  }

  public MotoCategoryPrice saveMotoCategoryPrice(MotoCategoryPrice price) {
    return motoCategoryPriceRepository.save(price);
  }

  public Optional<MotoCategoryPrice> updateMotoCategoryPrice(Long id, MotoCategoryPrice details) {
    return motoCategoryPriceRepository
        .findById(id)
        .map(
            existing -> {
              existing.setMotoCategory(details.getMotoCategory());
              existing.setCountry(details.getCountry());
              existing.setStartDate(details.getStartDate());
              existing.setEndDate(details.getEndDate());
              existing.setDailyPrice(details.getDailyPrice());
              existing.setKmPrice(details.getKmPrice());
              return motoCategoryPriceRepository.save(existing);
            });
  }

  public boolean deleteMotoCategoryPrice(Long id) {
    if (!motoCategoryPriceRepository.existsById(id)) {
      return false;
    }
    motoCategoryPriceRepository.deleteById(id);
    return true;
  }

  // --- Locations ---
  public Optional<MotoLocation> getMotoLocationById(Long id) {
    return motoLocationRepository.findById(id);
  }

  public List<MotoLocation> getLocationsForCategory(Long categoryId) {
    log.info("Fetching locations for moto category id: {}", categoryId);
    return motoLocationRepository.findByMotoCategoryId(categoryId);
  }

  public List<MotoLocation> getAllMotoLocations() {
    return motoLocationRepository.findAll();
  }

  public MotoLocation saveMotoLocation(MotoLocation location) {
    return motoLocationRepository.save(location);
  }

  public Optional<MotoLocation> updateMotoLocation(Long id, MotoLocation details) {
    return motoLocationRepository
        .findById(id)
        .map(
            existing -> {
              existing.setMotoCategory(details.getMotoCategory());
              existing.setBrand(details.getBrand());
              existing.setModel(details.getModel());
              existing.setCount(details.getCount());
              return motoLocationRepository.save(existing);
            });
  }

  public boolean deleteMotoLocation(Long id) {
    if (!motoLocationRepository.existsById(id)) {
      return false;
    }
    motoLocationRepository.deleteById(id);
    return true;
  }
}
