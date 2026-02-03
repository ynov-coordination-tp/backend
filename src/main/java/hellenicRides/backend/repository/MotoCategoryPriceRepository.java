package hellenicRides.backend.repository;

import hellenicRides.backend.entity.MotoCategoryPrice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoCategoryPriceRepository extends JpaRepository<MotoCategoryPrice, Long> {
  List<MotoCategoryPrice> findByMotoCategoryId(Long motoCategoryId);

  List<MotoCategoryPrice> findByMotoCategoryIdAndCountry(Long motoCategoryId, String country);
}
