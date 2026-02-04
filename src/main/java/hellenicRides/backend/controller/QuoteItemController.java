package hellenicRides.backend.controller;

import hellenicRides.backend.entity.QuoteItem;
import hellenicRides.backend.service.QuoteItemService;
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
@RequestMapping("/api/quote-items")
@RequiredArgsConstructor
@Tag(name = "Quote Items", description = "CRUD operations for quote items.")
public class QuoteItemController {

  private final QuoteItemService quoteItemService;

  @GetMapping
  @Operation(summary = "List quote items", description = "Public endpoint to list all quote items.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of quote items.")})
  public ResponseEntity<List<QuoteItem>> getAllQuoteItems() {
    return ResponseEntity.ok(quoteItemService.getAllQuoteItems());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get quote item by id",
      description = "Public endpoint to fetch a quote item by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Quote item found."),
    @ApiResponse(responseCode = "404", description = "Quote item not found.")
  })
  public ResponseEntity<QuoteItem> getQuoteItemById(
      @Parameter(description = "Quote item id") @PathVariable Long id) {
    return quoteItemService
        .getQuoteItemById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/quote/{quoteId}")
  @Operation(
      summary = "List quote items by quote",
      description = "Public endpoint to list quote items for a specific quote.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of quote items.")})
  public ResponseEntity<List<QuoteItem>> getQuoteItemsByQuoteId(
      @Parameter(description = "Quote id") @PathVariable Long quoteId) {
    return ResponseEntity.ok(quoteItemService.getQuoteItemsByQuoteId(quoteId));
  }

  @PostMapping
  @Operation(summary = "Create quote item", description = "Create a new quote item.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Quote item created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<QuoteItem> createQuoteItem(@RequestBody QuoteItem quoteItem) {
    QuoteItem created = quoteItemService.saveQuoteItem(quoteItem);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update quote item", description = "Update an existing quote item by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Quote item updated."),
    @ApiResponse(responseCode = "404", description = "Quote item not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<QuoteItem> updateQuoteItem(
      @Parameter(description = "Quote item id") @PathVariable Long id,
      @RequestBody QuoteItem quoteItem) {
    return quoteItemService
        .updateQuoteItem(id, quoteItem)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete quote item", description = "Delete a quote item by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Quote item deleted."),
    @ApiResponse(responseCode = "404", description = "Quote item not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteQuoteItem(
      @Parameter(description = "Quote item id") @PathVariable Long id) {
    if (!quoteItemService.deleteQuoteItem(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
