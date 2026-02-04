package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Accommodation;
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
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
@Tag(name = "Accommodations", description = "CRUD operations for accommodations.")
public class AccommodationController {

  private final AccommodationService accommodationService;

  @GetMapping
  @Operation(
      summary = "List accommodations",
      description = "Public endpoint to list all accommodations.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of accommodations.")})
  public ResponseEntity<List<Accommodation>> getAllAccommodations() {
    return ResponseEntity.ok(accommodationService.getAllAccommodations());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get accommodation by id",
      description = "Public endpoint to fetch an accommodation by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Accommodation found."),
    @ApiResponse(responseCode = "404", description = "Accommodation not found.")
  })
  public ResponseEntity<Accommodation> getAccommodationById(
      @Parameter(description = "Accommodation id") @PathVariable Long id) {
    return accommodationService
        .getAccommodationById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create accommodation", description = "Create a new accommodation.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Accommodation created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Accommodation> createAccommodation(
      @RequestBody Accommodation accommodation) {
    Accommodation created = accommodationService.saveAccommodation(accommodation);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update accommodation",
      description = "Update an existing accommodation by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Accommodation updated."),
    @ApiResponse(responseCode = "404", description = "Accommodation not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Accommodation> updateAccommodation(
      @Parameter(description = "Accommodation id") @PathVariable Long id,
      @RequestBody Accommodation accommodation) {
    return accommodationService
        .updateAccommodation(id, accommodation)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete accommodation", description = "Delete an accommodation by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Accommodation deleted."),
    @ApiResponse(responseCode = "404", description = "Accommodation not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteAccommodation(
      @Parameter(description = "Accommodation id") @PathVariable Long id) {
    if (accommodationService.getAccommodationById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    accommodationService.deleteAccommodation(id);
    return ResponseEntity.noContent().build();
  }
}
