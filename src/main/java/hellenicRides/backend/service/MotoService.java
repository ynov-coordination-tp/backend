package hellenicRides.backend.service;

import hellenicRides.backend.entity.MotoCategory;
import hellenicRides.backend.entity.MotoCategoryPrice;
import hellenicRides.backend.entity.MotoLocation;
import hellenicRides.backend.repository.MotoCategoryPriceRepository;
import hellenicRides.backend.repository.MotoCategoryRepository;
import hellenicRides.backend.repository.MotoLocationRepository;
import java.util.List;
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

  public MotoCategory saveMotoCategory(MotoCategory category) {
    return motoCategoryRepository.save(category);
  }

  // --- Prices ---
  public List<MotoCategoryPrice> getPricesForCategory(Long categoryId) {
    log.info("Fetching prices for moto category id: {}", categoryId);
    return motoCategoryPriceRepository.findByMotoCategoryId(categoryId);
  }

  public MotoCategoryPrice saveMotoCategoryPrice(MotoCategoryPrice price) {
    return motoCategoryPriceRepository.save(price);
  }

  // --- Locations ---
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
}
