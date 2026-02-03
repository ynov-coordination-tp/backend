package hellenicRides.backend.repository;

import hellenicRides.backend.entity.Quote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
  Optional<Quote> findByQuoteNumber(String quoteNumber);

  List<Quote> findByCustomerId(Long customerId);

  List<Quote> findByStatus(String status);
}
