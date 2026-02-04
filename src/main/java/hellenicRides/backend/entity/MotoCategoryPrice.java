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
@Table(name = "moto_category_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "MotoCategoryPrice",
    description = "Pricing for a moto category by country and date range.")
public class MotoCategoryPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "moto_category_id", nullable = false)
  @Schema(description = "Moto category")
  private MotoCategory motoCategory;

  @Column(nullable = false)
  @Schema(description = "Country", example = "Greece")
  private String country;

  @Column(name = "start_date", nullable = false)
  @Schema(description = "Start date (inclusive)", example = "2026-05-01")
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  @Schema(description = "End date (inclusive)", example = "2026-05-31")
  private LocalDate endDate;

  @Column(name = "daily_price", nullable = false)
  @Schema(description = "Daily price", example = "90.00")
  private BigDecimal dailyPrice;

  @Column(name = "km_price")
  @Schema(description = "Per km price", example = "0.50")
  private BigDecimal kmPrice;
}
