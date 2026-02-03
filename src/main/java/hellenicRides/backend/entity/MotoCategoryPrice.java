package hellenicRides.backend.entity;

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
public class MotoCategoryPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "moto_category_id", nullable = false)
  private MotoCategory motoCategory;

  @Column(nullable = false)
  private String country;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "daily_price", nullable = false)
  private BigDecimal dailyPrice;

  @Column(name = "km_price")
  private BigDecimal kmPrice;
}
