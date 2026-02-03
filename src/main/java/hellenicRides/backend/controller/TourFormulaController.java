package hellenicRides.backend.controller;

import hellenicRides.backend.entity.TourFormula;
import hellenicRides.backend.service.TourFormulaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tour-formulas")
@RequiredArgsConstructor
public class TourFormulaController {

  private final TourFormulaService tourFormulaService;

  @GetMapping
  public ResponseEntity<List<TourFormula>> getAllTourFormulas() {
    return ResponseEntity.ok(tourFormulaService.getAllTourFormulas());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TourFormula> getTourFormulaById(@PathVariable Long id) {
    return tourFormulaService
        .getTourFormulaById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // Optional: Get formulas for a specific tour
  @GetMapping("/tour/{tourId}")
  public ResponseEntity<List<TourFormula>> getFormulasByTour(@PathVariable Long tourId) {
    return ResponseEntity.ok(tourFormulaService.getTourFormulasByTourId(tourId));
  }
}
