package hellenicRides.backend.service;

import hellenicRides.backend.entity.QuoteItemOption;
import hellenicRides.backend.repository.QuoteItemOptionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteItemOptionService {

  private final QuoteItemOptionRepository quoteItemOptionRepository;

  public List<QuoteItemOption> getAllQuoteItemOptions() {
    return quoteItemOptionRepository.findAll();
  }

  public Optional<QuoteItemOption> getQuoteItemOptionById(Long id) {
    return quoteItemOptionRepository.findById(id);
  }

  public List<QuoteItemOption> getQuoteItemOptionsByQuoteItemId(Long quoteItemId) {
    return quoteItemOptionRepository.findByQuoteItemId(quoteItemId);
  }

  public QuoteItemOption saveQuoteItemOption(QuoteItemOption option) {
    return quoteItemOptionRepository.save(option);
  }

  public Optional<QuoteItemOption> updateQuoteItemOption(Long id, QuoteItemOption details) {
    return quoteItemOptionRepository
        .findById(id)
        .map(
            existing -> {
              existing.setQuoteItemId(details.getQuoteItemId());
              existing.setOptionId(details.getOptionId());
              existing.setQuantity(details.getQuantity());
              existing.setLockedPrice(details.getLockedPrice());
              return quoteItemOptionRepository.save(existing);
            });
  }

  public boolean deleteQuoteItemOption(Long id) {
    if (!quoteItemOptionRepository.existsById(id)) {
      return false;
    }
    quoteItemOptionRepository.deleteById(id);
    return true;
  }
}
