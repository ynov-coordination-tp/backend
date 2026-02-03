package hellenicRides.backend.controller;

import hellenicRides.backend.dto.QuoteCreateDto;
import hellenicRides.backend.dto.QuoteResponseDto;
import hellenicRides.backend.entity.Quote;
import hellenicRides.backend.service.QuoteService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

  private final QuoteService quoteService;

  @PostMapping
  public ResponseEntity<Quote> createQuote(@RequestBody QuoteCreateDto dto) {
    Quote quote = quoteService.createQuote(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(quote);
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuoteResponseDto> getQuoteById(@PathVariable Long id) {
    Optional<QuoteResponseDto> quote = quoteService.getQuoteById(id);
    return quote.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Quote> updateQuote(@PathVariable Long id, @RequestBody QuoteCreateDto dto) {
    Optional<Quote> quote = quoteService.updateQuote(id, dto);
    return quote.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}
