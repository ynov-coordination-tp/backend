package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Formula;
import hellenicRides.backend.service.FormulaService;
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
@RequestMapping("/api/formulas")
@RequiredArgsConstructor
@Tag(name = "Formulas", description = "CRUD operations for formulas.")
public class FormulaController {

  private final FormulaService formulaService;

  @GetMapping
  @Operation(summary = "List formulas", description = "Public endpoint to list all formulas.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of formulas.")})
  public ResponseEntity<List<Formula>> getAllFormulas() {
    return ResponseEntity.ok(formulaService.getAllFormulas());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get formula by id",
      description = "Public endpoint to fetch a formula by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Formula found."),
    @ApiResponse(responseCode = "404", description = "Formula not found.")
  })
  public ResponseEntity<Formula> getFormulaById(
      @Parameter(description = "Formula id") @PathVariable Long id) {
    return formulaService
        .getFormulaById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create formula", description = "Create a new formula.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Formula created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Formula> createFormula(@RequestBody Formula formula) {
    Formula created = formulaService.saveFormula(formula);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update formula", description = "Update an existing formula by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Formula updated."),
    @ApiResponse(responseCode = "404", description = "Formula not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Formula> updateFormula(
      @Parameter(description = "Formula id") @PathVariable Long id, @RequestBody Formula formula) {
    return formulaService
        .updateFormula(id, formula)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete formula", description = "Delete a formula by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Formula deleted."),
    @ApiResponse(responseCode = "404", description = "Formula not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteFormula(
      @Parameter(description = "Formula id") @PathVariable Long id) {
    if (!formulaService.deleteFormula(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
