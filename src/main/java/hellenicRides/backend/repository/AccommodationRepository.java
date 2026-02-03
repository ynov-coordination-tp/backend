package hellenicRides.backend.repository;

import hellenicRides.backend.entity.Accommodation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
  List<Accommodation> findByCountry(String country);

  List<Accommodation> findByCity(String city);
}
