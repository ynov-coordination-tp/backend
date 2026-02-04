package hellenicRides.backend.controller;

import hellenicRides.backend.entity.MotoLocation;
import hellenicRides.backend.service.MotoService;
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
@RequestMapping("/api/moto-locations")
@RequiredArgsConstructor
@Tag(name = "Moto Locations", description = "CRUD operations for moto locations.")
public class MotoLocationController {

  private final MotoService motoService;

  @GetMapping
  @Operation(
      summary = "List moto locations",
      description = "Public endpoint to list all moto locations.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of moto locations.")})
  public ResponseEntity<List<MotoLocation>> getAllMotoLocations() {
    return ResponseEntity.ok(motoService.getAllMotoLocations());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get moto location by id",
      description = "Public endpoint to fetch a moto location by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Moto location found."),
    @ApiResponse(responseCode = "404", description = "Moto location not found.")
  })
  public ResponseEntity<MotoLocation> getMotoLocationById(
      @Parameter(description = "Moto location id") @PathVariable Long id) {
    return motoService
        .getMotoLocationById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create moto location", description = "Create a new moto location.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Moto location created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<MotoLocation> createMotoLocation(@RequestBody MotoLocation motoLocation) {
    MotoLocation created = motoService.saveMotoLocation(motoLocation);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update moto location",
      description = "Update an existing moto location by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Moto location updated."),
    @ApiResponse(responseCode = "404", description = "Moto location not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<MotoLocation> updateMotoLocation(
      @Parameter(description = "Moto location id") @PathVariable Long id,
      @RequestBody MotoLocation motoLocation) {
    return motoService
        .updateMotoLocation(id, motoLocation)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete moto location", description = "Delete a moto location by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Moto location deleted."),
    @ApiResponse(responseCode = "404", description = "Moto location not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteMotoLocation(
      @Parameter(description = "Moto location id") @PathVariable Long id) {
    if (!motoService.deleteMotoLocation(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
