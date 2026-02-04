package hellenicRides.backend.controller;

import hellenicRides.backend.entity.MotoCategory;
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
@RequestMapping("/api/moto-categories")
@RequiredArgsConstructor
@Tag(name = "Moto Categories", description = "CRUD operations for moto categories.")
public class MotoCategoryController {

  private final MotoService motoService;

  @GetMapping
  @Operation(
      summary = "List moto categories",
      description = "Public endpoint to list all moto categories.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of moto categories.")})
  public ResponseEntity<List<MotoCategory>> getAllMotoCategories() {
    return ResponseEntity.ok(motoService.getAllMotoCategories());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get moto category by id",
      description = "Public endpoint to fetch a moto category by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Moto category found."),
    @ApiResponse(responseCode = "404", description = "Moto category not found.")
  })
  public ResponseEntity<MotoCategory> getMotoCategoryById(
      @Parameter(description = "Moto category id") @PathVariable Long id) {
    return motoService
        .getMotoCategoryById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create moto category", description = "Create a new moto category.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Moto category created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<MotoCategory> createMotoCategory(@RequestBody MotoCategory motoCategory) {
    MotoCategory created = motoService.saveMotoCategory(motoCategory);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update moto category",
      description = "Update an existing moto category by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Moto category updated."),
    @ApiResponse(responseCode = "404", description = "Moto category not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<MotoCategory> updateMotoCategory(
      @Parameter(description = "Moto category id") @PathVariable Long id,
      @RequestBody MotoCategory motoCategory) {
    return motoService
        .updateMotoCategory(id, motoCategory)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete moto category", description = "Delete a moto category by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Moto category deleted."),
    @ApiResponse(responseCode = "404", description = "Moto category not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteMotoCategory(
      @Parameter(description = "Moto category id") @PathVariable Long id) {
    if (!motoService.deleteMotoCategory(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
