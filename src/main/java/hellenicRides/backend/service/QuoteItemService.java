package hellenicRides.backend.service;

import hellenicRides.backend.entity.QuoteItem;
import hellenicRides.backend.repository.QuoteItemRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteItemService {

  private final QuoteItemRepository quoteItemRepository;

  public List<QuoteItem> getAllQuoteItems() {
    return quoteItemRepository.findAll();
  }

  public Optional<QuoteItem> getQuoteItemById(Long id) {
    return quoteItemRepository.findById(id);
  }

  public List<QuoteItem> getQuoteItemsByQuoteId(Long quoteId) {
    return quoteItemRepository.findByQuoteId(quoteId);
  }

  public QuoteItem saveQuoteItem(QuoteItem quoteItem) {
    return quoteItemRepository.save(quoteItem);
  }

  public Optional<QuoteItem> updateQuoteItem(Long id, QuoteItem details) {
    return quoteItemRepository
        .findById(id)
        .map(
            existing -> {
              existing.setQuoteId(details.getQuoteId());
              existing.setParticipantName(details.getParticipantName());
              existing.setMotoLocationId(details.getMotoLocationId());
              existing.setAccommodationId(details.getAccommodationId());
              existing.setLockedUnitPrice(details.getLockedUnitPrice());
              return quoteItemRepository.save(existing);
            });
  }

  public boolean deleteQuoteItem(Long id) {
    if (!quoteItemRepository.existsById(id)) {
      return false;
    }
    quoteItemRepository.deleteById(id);
    return true;
  }
}
