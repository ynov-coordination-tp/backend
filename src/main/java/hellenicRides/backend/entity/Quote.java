package hellenicRides.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quote_number", unique = true)
  private String quoteNumber;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "departure_date")
  private LocalDate departureDate;

  @Column(name = "return_date")
  private LocalDate returnDate;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "tour_package_id")
  private Long tourPackageId;

  @Column(name = "participant_count")
  private Integer participantCount;

  @Column(name = "locked_total_price", precision = 10, scale = 2)
  private BigDecimal lockedTotalPrice;

  @Column private String status; // DRAFT, CONFIRMED, CANCELLED

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    if (status == null) {
      status = "DRAFT";
    }
  }
}
