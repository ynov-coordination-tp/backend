package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tour_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "TourPrice", description = "Pricing for a tour formula by date range.")
public class TourPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tour_formula_id", nullable = false)
  @Schema(description = "Tour formula")
  private TourFormula tourFormula;

  @Column(name = "start_date", nullable = false)
  @Schema(description = "Start date (inclusive)", example = "2026-05-01")
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  @Schema(description = "End date (inclusive)", example = "2026-05-31")
  private LocalDate endDate;

  @Column(name = "base_price", nullable = false)
  @Schema(description = "Base price", example = "2500.00")
  private BigDecimal basePrice;
}
