package hellenicRides.backend.repository;

import hellenicRides.backend.entity.QuoteItemOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteItemOptionRepository extends JpaRepository<QuoteItemOption, Long> {
  List<QuoteItemOption> findByQuoteItemId(Long quoteItemId);

  void deleteByQuoteItemId(Long quoteItemId);
}
