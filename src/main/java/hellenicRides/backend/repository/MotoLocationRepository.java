package hellenicRides.backend.repository;

import hellenicRides.backend.entity.MotoLocation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoLocationRepository extends JpaRepository<MotoLocation, Long> {
  List<MotoLocation> findByMotoCategoryId(Long motoCategoryId);
}
