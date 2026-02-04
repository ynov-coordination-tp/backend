package hellenicRides.backend.controller;

import hellenicRides.backend.entity.TourFormula;
import hellenicRides.backend.service.TourFormulaService;
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
@RequestMapping("/api/tour-formulas")
@RequiredArgsConstructor
@Tag(name = "Tour Formulas", description = "CRUD operations for tour formulas.")
public class TourFormulaController {

  private final TourFormulaService tourFormulaService;

  @GetMapping
  @Operation(
      summary = "List tour formulas",
      description = "Public endpoint to list all tour formulas.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of tour formulas.")})
  public ResponseEntity<List<TourFormula>> getAllTourFormulas() {
    return ResponseEntity.ok(tourFormulaService.getAllTourFormulas());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get tour formula by id",
      description = "Public endpoint to fetch a tour formula by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tour formula found."),
    @ApiResponse(responseCode = "404", description = "Tour formula not found.")
  })
  public ResponseEntity<TourFormula> getTourFormulaById(
      @Parameter(description = "Tour formula id") @PathVariable Long id) {
    return tourFormulaService
        .getTourFormulaById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // Optional: Get formulas for a specific tour
  @GetMapping("/tour/{tourId}")
  @Operation(
      summary = "List formulas by tour",
      description = "Public endpoint to list formulas for a specific tour.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of tour formulas.")})
  public ResponseEntity<List<TourFormula>> getFormulasByTour(
      @Parameter(description = "Tour id") @PathVariable Long tourId) {
    return ResponseEntity.ok(tourFormulaService.getTourFormulasByTourId(tourId));
  }

  @PostMapping
  @Operation(summary = "Create tour formula", description = "Create a new tour formula link.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Tour formula created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<TourFormula> createTourFormula(@RequestBody TourFormula tourFormula) {
    TourFormula created = tourFormulaService.saveTourFormula(tourFormula);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update tour formula",
      description = "Update an existing tour formula by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tour formula updated."),
    @ApiResponse(responseCode = "404", description = "Tour formula not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<TourFormula> updateTourFormula(
      @Parameter(description = "Tour formula id") @PathVariable Long id,
      @RequestBody TourFormula tourFormula) {
    return tourFormulaService
        .updateTourFormula(id, tourFormula)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete tour formula", description = "Delete a tour formula by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Tour formula deleted."),
    @ApiResponse(responseCode = "404", description = "Tour formula not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteTourFormula(
      @Parameter(description = "Tour formula id") @PathVariable Long id) {
    if (!tourFormulaService.deleteTourFormula(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
