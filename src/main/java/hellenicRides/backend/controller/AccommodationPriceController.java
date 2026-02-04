package hellenicRides.backend.controller;

import hellenicRides.backend.entity.AccommodationPrice;
import hellenicRides.backend.service.AccommodationService;
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
@RequestMapping("/api/accommodation-prices")
@RequiredArgsConstructor
@Tag(name = "Accommodation Prices", description = "CRUD operations for accommodation prices.")
public class AccommodationPriceController {

  private final AccommodationService accommodationService;

  @GetMapping
  @Operation(
      summary = "List accommodation prices",
      description = "Public endpoint to list all accommodation prices.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of accommodation prices.")})
  public ResponseEntity<List<AccommodationPrice>> getAllAccommodationPrices() {
    return ResponseEntity.ok(accommodationService.getAllAccommodationPrices());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get accommodation price by id",
      description = "Public endpoint to fetch an accommodation price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Accommodation price found."),
    @ApiResponse(responseCode = "404", description = "Accommodation price not found.")
  })
  public ResponseEntity<AccommodationPrice> getAccommodationPriceById(
      @Parameter(description = "Accommodation price id") @PathVariable Long id) {
    return accommodationService
        .getAccommodationPriceById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/accommodation/{accommodationId}")
  @Operation(
      summary = "List accommodation prices by accommodation",
      description = "Public endpoint to list accommodation prices for a specific accommodation.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of accommodation prices.")})
  public ResponseEntity<List<AccommodationPrice>> getAccommodationPricesByAccommodationId(
      @Parameter(description = "Accommodation id") @PathVariable Long accommodationId) {
    return ResponseEntity.ok(
        accommodationService.getAccommodationPricesByAccommodationId(accommodationId));
  }

  @PostMapping
  @Operation(
      summary = "Create accommodation price",
      description = "Create a new accommodation price.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Accommodation price created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<AccommodationPrice> createAccommodationPrice(
      @RequestBody AccommodationPrice accommodationPrice) {
    AccommodationPrice created = accommodationService.saveAccommodationPrice(accommodationPrice);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update accommodation price",
      description = "Update an existing accommodation price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Accommodation price updated."),
    @ApiResponse(responseCode = "404", description = "Accommodation price not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<AccommodationPrice> updateAccommodationPrice(
      @Parameter(description = "Accommodation price id") @PathVariable Long id,
      @RequestBody AccommodationPrice accommodationPrice) {
    return accommodationService
        .updateAccommodationPrice(id, accommodationPrice)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete accommodation price",
      description = "Delete an accommodation price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Accommodation price deleted."),
    @ApiResponse(responseCode = "404", description = "Accommodation price not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteAccommodationPrice(
      @Parameter(description = "Accommodation price id") @PathVariable Long id) {
    if (!accommodationService.deleteAccommodationPrice(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
