package hellenicRides.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accommodation_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "accommodation_id", nullable = false)
  private Long accommodationId;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "nightly_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal nightlyPrice;

  @Column(name = "room_type", nullable = false)
  private String roomType; // SINGLE, COUPLE, SHARED
}
