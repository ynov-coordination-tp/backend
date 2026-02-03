package hellenicRides.backend.repository;

import hellenicRides.backend.entity.AccommodationPrice;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationPriceRepository extends JpaRepository<AccommodationPrice, Long> {

  List<AccommodationPrice> findByAccommodationId(Long accommodationId);

  @Query(
      "SELECT ap FROM AccommodationPrice ap WHERE ap.accommodationId = :accommodationId "
          + "AND ap.roomType = :roomType "
          + "AND ap.startDate <= :date AND ap.endDate >= :date")
  Optional<AccommodationPrice> findPriceForDate(
      @Param("accommodationId") Long accommodationId,
      @Param("roomType") String roomType,
      @Param("date") LocalDate date);
}
