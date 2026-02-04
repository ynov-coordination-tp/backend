package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Option;
import hellenicRides.backend.service.OptionService;
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
@RequestMapping("/api/options")
@RequiredArgsConstructor
@Tag(name = "Options", description = "CRUD operations for options.")
public class OptionController {

  private final OptionService optionService;

  @GetMapping
  @Operation(summary = "List options", description = "Public endpoint to list all options.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of options.")})
  public ResponseEntity<List<Option>> getAllOptions() {
    return ResponseEntity.ok(optionService.getAllOptions());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get option by id",
      description = "Public endpoint to fetch an option by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Option found."),
    @ApiResponse(responseCode = "404", description = "Option not found.")
  })
  public ResponseEntity<Option> getOptionById(
      @Parameter(description = "Option id") @PathVariable Long id) {
    return optionService
        .getOptionById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create option", description = "Create a new option.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Option created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Option> createOption(@RequestBody Option option) {
    Option created = optionService.saveOption(option);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update option", description = "Update an existing option by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Option updated."),
    @ApiResponse(responseCode = "404", description = "Option not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Option> updateOption(
      @Parameter(description = "Option id") @PathVariable Long id, @RequestBody Option option) {
    return optionService
        .updateOption(id, option)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete option", description = "Delete an option by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Option deleted."),
    @ApiResponse(responseCode = "404", description = "Option not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteOption(
      @Parameter(description = "Option id") @PathVariable Long id) {
    if (!optionService.deleteOption(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
