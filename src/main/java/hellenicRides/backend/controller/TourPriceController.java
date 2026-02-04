package hellenicRides.backend.controller;

import hellenicRides.backend.entity.TourPrice;
import hellenicRides.backend.service.TourPriceService;
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
@RequestMapping("/api/tour-prices")
@RequiredArgsConstructor
@Tag(name = "Tour Prices", description = "CRUD operations for tour prices.")
public class TourPriceController {

  private final TourPriceService tourPriceService;

  @GetMapping
  @Operation(summary = "List tour prices", description = "Public endpoint to list all tour prices.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of tour prices.")})
  public ResponseEntity<List<TourPrice>> getAllTourPrices() {
    return ResponseEntity.ok(tourPriceService.getAllTourPrices());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get tour price by id",
      description = "Public endpoint to fetch a tour price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tour price found."),
    @ApiResponse(responseCode = "404", description = "Tour price not found.")
  })
  public ResponseEntity<TourPrice> getTourPriceById(
      @Parameter(description = "Tour price id") @PathVariable Long id) {
    return tourPriceService
        .getTourPriceById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/formula/{tourFormulaId}")
  @Operation(
      summary = "List prices by tour formula",
      description = "Public endpoint to list prices for a specific tour formula.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of tour prices.")})
  public ResponseEntity<List<TourPrice>> getTourPricesByFormula(
      @Parameter(description = "Tour formula id") @PathVariable Long tourFormulaId) {
    return ResponseEntity.ok(tourPriceService.getPricesForFormula(tourFormulaId));
  }

  @PostMapping
  @Operation(summary = "Create tour price", description = "Create a new tour price.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Tour price created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<TourPrice> createTourPrice(@RequestBody TourPrice tourPrice) {
    TourPrice created = tourPriceService.saveTourPrice(tourPrice);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update tour price", description = "Update an existing tour price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Tour price updated."),
    @ApiResponse(responseCode = "404", description = "Tour price not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<TourPrice> updateTourPrice(
      @Parameter(description = "Tour price id") @PathVariable Long id,
      @RequestBody TourPrice tourPrice) {
    return tourPriceService
        .updateTourPrice(id, tourPrice)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete tour price", description = "Delete a tour price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Tour price deleted."),
    @ApiResponse(responseCode = "404", description = "Tour price not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteTourPrice(
      @Parameter(description = "Tour price id") @PathVariable Long id) {
    if (!tourPriceService.deleteTourPrice(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
