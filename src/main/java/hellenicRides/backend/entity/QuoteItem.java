package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quote_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "QuoteItem", description = "Quote item line.")
public class QuoteItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(name = "quote_id", nullable = false)
  @Schema(description = "Quote id", example = "10")
  private Long quoteId;

  @Column(name = "participant_name")
  @Schema(description = "Participant name", example = "John Doe")
  private String participantName;

  @Column(name = "moto_location_id")
  @Schema(description = "Moto location id", example = "5")
  private Long motoLocationId; // nullable for Poseidon formula

  @Column(name = "accommodation_id")
  @Schema(description = "Accommodation id", example = "3")
  private Long accommodationId;

  @Column(name = "locked_unit_price", precision = 10, scale = 2)
  @Schema(
      description = "Locked unit price",
      example = "1200.00",
      accessMode = Schema.AccessMode.READ_ONLY)
  private BigDecimal lockedUnitPrice;
}
