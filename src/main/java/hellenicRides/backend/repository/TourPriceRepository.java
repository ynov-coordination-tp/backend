package hellenicRides.backend.repository;

import hellenicRides.backend.entity.TourPrice;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourPriceRepository extends JpaRepository<TourPrice, Long> {
  // Find prices for a specific tour formula that cover a given date
  List<TourPrice> findByTourFormulaIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
      Long tourFormulaId, LocalDate date, LocalDate date2);

  // Simple finder
  List<TourPrice> findByTourFormulaId(Long tourFormulaId);
}
