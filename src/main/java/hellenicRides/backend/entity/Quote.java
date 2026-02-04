package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Quote", description = "Quote aggregate header.")
public class Quote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(name = "quote_number", unique = true)
  @Schema(
      description = "Quote number",
      example = "QT-AB12CD34",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String quoteNumber;

  @Column(name = "created_at")
  @Schema(
      description = "Creation date time",
      example = "2026-02-04T10:15:30",
      accessMode = Schema.AccessMode.READ_ONLY)
  private LocalDateTime createdAt;

  @Column(name = "departure_date")
  @Schema(description = "Departure date", example = "2026-06-01")
  private LocalDate departureDate;

  @Column(name = "return_date")
  @Schema(description = "Return date", example = "2026-06-10")
  private LocalDate returnDate;

  @Column(name = "customer_id")
  @Schema(description = "Customer id", example = "100")
  private Long customerId;

  @Column(name = "tour_package_id")
  @Schema(description = "Tour package id", example = "200")
  private Long tourPackageId;

  @Column(name = "participant_count")
  @Schema(description = "Participant count", example = "2")
  private Integer participantCount;

  @Column(name = "locked_total_price", precision = 10, scale = 2)
  @Schema(
      description = "Locked total price",
      example = "3500.00",
      accessMode = Schema.AccessMode.READ_ONLY)
  private BigDecimal lockedTotalPrice;

  @Column
  @Schema(description = "Quote status", example = "DRAFT")
  private String status; // DRAFT, CONFIRMED, CANCELLED

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    if (status == null) {
      status = "DRAFT";
    }
  }
}
