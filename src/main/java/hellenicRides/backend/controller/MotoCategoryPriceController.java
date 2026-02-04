package hellenicRides.backend.controller;

import hellenicRides.backend.entity.MotoCategoryPrice;
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
@RequestMapping("/api/moto-category-prices")
@RequiredArgsConstructor
@Tag(name = "Moto Category Prices", description = "CRUD operations for moto category prices.")
public class MotoCategoryPriceController {

  private final MotoService motoService;

  @GetMapping
  @Operation(
      summary = "List moto category prices",
      description = "Public endpoint to list all moto category prices.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of moto category prices.")})
  public ResponseEntity<List<MotoCategoryPrice>> getAllMotoCategoryPrices() {
    return ResponseEntity.ok(motoService.getAllMotoCategoryPrices());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get moto category price by id",
      description = "Public endpoint to fetch a moto category price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Moto category price found."),
    @ApiResponse(responseCode = "404", description = "Moto category price not found.")
  })
  public ResponseEntity<MotoCategoryPrice> getMotoCategoryPriceById(
      @Parameter(description = "Moto category price id") @PathVariable Long id) {
    return motoService
        .getMotoCategoryPriceById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/category/{categoryId}")
  @Operation(
      summary = "List prices by moto category",
      description = "Public endpoint to list prices for a specific moto category.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of moto category prices.")})
  public ResponseEntity<List<MotoCategoryPrice>> getMotoCategoryPricesByCategory(
      @Parameter(description = "Moto category id") @PathVariable Long categoryId) {
    return ResponseEntity.ok(motoService.getPricesForCategory(categoryId));
  }

  @PostMapping
  @Operation(
      summary = "Create moto category price",
      description = "Create a new moto category price.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Moto category price created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<MotoCategoryPrice> createMotoCategoryPrice(
      @RequestBody MotoCategoryPrice motoCategoryPrice) {
    MotoCategoryPrice created = motoService.saveMotoCategoryPrice(motoCategoryPrice);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update moto category price",
      description = "Update an existing moto category price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Moto category price updated."),
    @ApiResponse(responseCode = "404", description = "Moto category price not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<MotoCategoryPrice> updateMotoCategoryPrice(
      @Parameter(description = "Moto category price id") @PathVariable Long id,
      @RequestBody MotoCategoryPrice motoCategoryPrice) {
    return motoService
        .updateMotoCategoryPrice(id, motoCategoryPrice)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete moto category price",
      description = "Delete a moto category price by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Moto category price deleted."),
    @ApiResponse(responseCode = "404", description = "Moto category price not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteMotoCategoryPrice(
      @Parameter(description = "Moto category price id") @PathVariable Long id) {
    if (!motoService.deleteMotoCategoryPrice(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
