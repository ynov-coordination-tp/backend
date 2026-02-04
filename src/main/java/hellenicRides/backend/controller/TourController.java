package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Tour;
import hellenicRides.backend.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
@Tag(name = "Tours", description = "CRUD operations for tours.")
public class TourController {

  private final TourService tourService;

  @GetMapping
  @Operation(summary = "List tours", description = "Public endpoint to list all tours.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of tours.")})
  public ResponseEntity<List<Tour>> getAllTours() {
    return ResponseEntity.ok(tourService.getAllTours());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get tour by id", description = "Public endpoint to fetch a tour by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tour found."),
    @ApiResponse(responseCode = "404", description = "Tour not found.")
  })
  public ResponseEntity<Tour> getTourById(
      @Parameter(description = "Tour id") @PathVariable Long id) {
    return tourService
        .getTourById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create tour", description = "Create a new tour.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Tour created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Tour> createTour(@RequestBody Tour tour) {
    Tour created = tourService.saveTour(tour);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update tour", description = "Update an existing tour by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tour updated."),
    @ApiResponse(responseCode = "404", description = "Tour not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Tour> updateTour(
      @Parameter(description = "Tour id") @PathVariable Long id, @RequestBody Tour tour) {
    return tourService
        .updateTour(id, tour)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete tour", description = "Delete a tour by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Tour deleted."),
    @ApiResponse(responseCode = "404", description = "Tour not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteTour(
      @Parameter(description = "Tour id") @PathVariable Long id) {
    if (tourService.getTourById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    tourService.deleteTour(id);
    return ResponseEntity.noContent().build();
  }
}
