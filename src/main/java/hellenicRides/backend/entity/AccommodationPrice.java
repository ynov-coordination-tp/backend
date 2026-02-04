package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "AccommodationPrice", description = "Pricing for accommodations.")
public class AccommodationPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(name = "accommodation_id", nullable = false)
  @Schema(description = "Accommodation id", example = "10")
  private Long accommodationId;

  @Column(name = "start_date", nullable = false)
  @Schema(description = "Start date (inclusive)", example = "2026-05-01")
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  @Schema(description = "End date (inclusive)", example = "2026-05-31")
  private LocalDate endDate;

  @Column(name = "nightly_price", nullable = false, precision = 10, scale = 2)
  @Schema(description = "Nightly price", example = "120.00")
  private BigDecimal nightlyPrice;

  @Column(name = "room_type", nullable = false)
  @Schema(description = "Room type", example = "SINGLE")
  private String roomType; // SINGLE, COUPLE, SHARED
}
