package hellenicRides.backend.service;

import hellenicRides.backend.entity.TourFormula;
import hellenicRides.backend.repository.TourFormulaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourFormulaService {

  private final TourFormulaRepository tourFormulaRepository;

  public List<TourFormula> getAllTourFormulas() {
    log.info("Fetching all tour formulas");
    return tourFormulaRepository.findAll();
  }

  public List<TourFormula> getTourFormulasByTourId(Long tourId) {
    log.info("Fetching formulas for tour id: {}", tourId);
    return tourFormulaRepository.findByTourId(tourId);
  }

  public List<TourFormula> getActiveTourFormulasByTourId(Long tourId) {
    log.info("Fetching active formulas for tour id: {}", tourId);
    return tourFormulaRepository.findByTourIdAndIsActiveTrue(tourId);
  }

  public Optional<TourFormula> getTourFormulaById(Long id) {
    log.info("Fetching tour formula with id: {}", id);
    return tourFormulaRepository.findById(id);
  }

  public TourFormula saveTourFormula(TourFormula tourFormula) {
    log.info("Saving tour formula connection");
    return tourFormulaRepository.save(tourFormula);
  }

  public Optional<TourFormula> updateTourFormula(Long id, TourFormula details) {
    return tourFormulaRepository
        .findById(id)
        .map(
            existing -> {
              existing.setTour(details.getTour());
              existing.setFormula(details.getFormula());
              existing.setIsActive(details.getIsActive());
              return tourFormulaRepository.save(existing);
            });
  }

  public boolean deleteTourFormula(Long id) {
    if (!tourFormulaRepository.existsById(id)) {
      return false;
    }
    tourFormulaRepository.deleteById(id);
    return true;
  }
}
