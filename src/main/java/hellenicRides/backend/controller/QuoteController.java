package hellenicRides.backend.controller;

import hellenicRides.backend.dto.QuoteCreateDto;
import hellenicRides.backend.dto.QuoteResponseDto;
import hellenicRides.backend.entity.Quote;
import hellenicRides.backend.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
@Tag(name = "Quotes", description = "CRUD operations for quotes.")
public class QuoteController {

  private final QuoteService quoteService;

  @PostMapping
  @Operation(summary = "Create quote", description = "Public endpoint to create a new quote.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Quote created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  public ResponseEntity<Quote> createQuote(@RequestBody QuoteCreateDto dto) {
    Quote quote = quoteService.createQuote(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(quote);
  }

  @GetMapping
  @Operation(summary = "List quotes", description = "Public endpoint to list all quotes.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of quotes.")})
  public ResponseEntity<List<Quote>> getAllQuotes() {
    return ResponseEntity.ok(quoteService.getAllQuotes());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get quote by id", description = "Public endpoint to fetch a quote by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Quote found."),
    @ApiResponse(responseCode = "404", description = "Quote not found.")
  })
  public ResponseEntity<QuoteResponseDto> getQuoteById(
      @Parameter(description = "Quote id") @PathVariable Long id) {
    Optional<QuoteResponseDto> quote = quoteService.getQuoteById(id);
    return quote.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update quote", description = "Update an existing quote by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Quote updated."),
    @ApiResponse(responseCode = "404", description = "Quote not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Quote> updateQuote(
      @Parameter(description = "Quote id") @PathVariable Long id, @RequestBody QuoteCreateDto dto) {
    Optional<Quote> quote = quoteService.updateQuote(id, dto);
    return quote.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete quote", description = "Delete a quote by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Quote deleted."),
    @ApiResponse(responseCode = "404", description = "Quote not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteQuote(
      @Parameter(description = "Quote id") @PathVariable Long id) {
    if (!quoteService.deleteQuote(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
