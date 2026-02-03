package hellenicRides.backend.repository;

import hellenicRides.backend.entity.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
  List<Option> findByTargetType(String targetType);
}
