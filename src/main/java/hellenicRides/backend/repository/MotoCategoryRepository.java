package hellenicRides.backend.repository;

import hellenicRides.backend.entity.MotoCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoCategoryRepository extends JpaRepository<MotoCategory, Long> {}
