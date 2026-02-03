package hellenicRides.backend.repository;

import hellenicRides.backend.entity.TourFormula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourFormulaRepository extends JpaRepository<TourFormula, Long> {
  List<TourFormula> findByTourId(Long tourId);

  List<TourFormula> findByTourIdAndIsActiveTrue(Long tourId);
}
