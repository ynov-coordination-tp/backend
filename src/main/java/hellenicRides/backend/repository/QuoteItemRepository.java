package hellenicRides.backend.repository;

import hellenicRides.backend.entity.QuoteItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteItemRepository extends JpaRepository<QuoteItem, Long> {
  List<QuoteItem> findByQuoteId(Long quoteId);

  void deleteByQuoteId(Long quoteId);
}
