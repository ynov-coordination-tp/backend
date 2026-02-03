package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Accommodation;
import hellenicRides.backend.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

  private final AccommodationService accommodationService;

  @GetMapping
  public ResponseEntity<List<Accommodation>> getAllAccommodations() {
    return ResponseEntity.ok(accommodationService.getAllAccommodations());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Accommodation> getAccommodationById(@PathVariable Long id) {
    return accommodationService
        .getAccommodationById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
