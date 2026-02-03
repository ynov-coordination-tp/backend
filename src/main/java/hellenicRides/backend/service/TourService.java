package hellenicRides.backend.service;

import hellenicRides.backend.entity.Tour;
import hellenicRides.backend.repository.TourRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourService {

  private final TourRepository tourRepository;

  public List<Tour> getAllTours() {
    log.info("Fetching all tours");
    return tourRepository.findAll();
  }

  public Optional<Tour> getTourById(Long id) {
    log.info("Fetching tour with id: {}", id);
    return tourRepository.findById(id);
  }

  public Tour saveTour(Tour tour) {
    log.info("Saving tour: {}", tour.getName());
    return tourRepository.save(tour);
  }

  public void deleteTour(Long id) {
    log.info("Deleting tour with id: {}", id);
    tourRepository.deleteById(id);
  }
}
