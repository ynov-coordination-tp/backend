package hellenicRides.backend.controller;

import hellenicRides.backend.entity.QuoteItemOption;
import hellenicRides.backend.service.QuoteItemOptionService;
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
@RequestMapping("/api/quote-item-options")
@RequiredArgsConstructor
@Tag(name = "Quote Item Options", description = "CRUD operations for quote item options.")
public class QuoteItemOptionController {

  private final QuoteItemOptionService quoteItemOptionService;

  @GetMapping
  @Operation(
      summary = "List quote item options",
      description = "Public endpoint to list all quote item options.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of quote item options.")})
  public ResponseEntity<List<QuoteItemOption>> getAllQuoteItemOptions() {
    return ResponseEntity.ok(quoteItemOptionService.getAllQuoteItemOptions());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get quote item option by id",
      description = "Public endpoint to fetch a quote item option by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Quote item option found."),
    @ApiResponse(responseCode = "404", description = "Quote item option not found.")
  })
  public ResponseEntity<QuoteItemOption> getQuoteItemOptionById(
      @Parameter(description = "Quote item option id") @PathVariable Long id) {
    return quoteItemOptionService
        .getQuoteItemOptionById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/quote-item/{quoteItemId}")
  @Operation(
      summary = "List options by quote item",
      description = "Public endpoint to list options for a specific quote item.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of quote item options.")})
  public ResponseEntity<List<QuoteItemOption>> getQuoteItemOptionsByQuoteItemId(
      @Parameter(description = "Quote item id") @PathVariable Long quoteItemId) {
    return ResponseEntity.ok(quoteItemOptionService.getQuoteItemOptionsByQuoteItemId(quoteItemId));
  }

  @PostMapping
  @Operation(summary = "Create quote item option", description = "Create a new quote item option.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Quote item option created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<QuoteItemOption> createQuoteItemOption(
      @RequestBody QuoteItemOption option) {
    QuoteItemOption created = quoteItemOptionService.saveQuoteItemOption(option);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update quote item option",
      description = "Update an existing quote item option by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Quote item option updated."),
    @ApiResponse(responseCode = "404", description = "Quote item option not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<QuoteItemOption> updateQuoteItemOption(
      @Parameter(description = "Quote item option id") @PathVariable Long id,
      @RequestBody QuoteItemOption option) {
    return quoteItemOptionService
        .updateQuoteItemOption(id, option)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete quote item option",
      description = "Delete a quote item option by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Quote item option deleted."),
    @ApiResponse(responseCode = "404", description = "Quote item option not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteQuoteItemOption(
      @Parameter(description = "Quote item option id") @PathVariable Long id) {
    if (!quoteItemOptionService.deleteQuoteItemOption(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
