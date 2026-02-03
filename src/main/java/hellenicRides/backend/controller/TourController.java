package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Tour;
import hellenicRides.backend.service.TourService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
public class TourController {

  private final TourService tourService;

  @GetMapping
  public ResponseEntity<List<Tour>> getAllTours() {
    return ResponseEntity.ok(tourService.getAllTours());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tour> getTourById(@PathVariable Long id) {
    return tourService
        .getTourById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
