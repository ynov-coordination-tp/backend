package hellenicRides.backend.entity;

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
public class QuoteItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quote_id", nullable = false)
  private Long quoteId;

  @Column(name = "participant_name")
  private String participantName;

  @Column(name = "moto_location_id")
  private Long motoLocationId; // nullable for Poseidon formula

  @Column(name = "accommodation_id")
  private Long accommodationId;

  @Column(name = "locked_unit_price", precision = 10, scale = 2)
  private BigDecimal lockedUnitPrice;
}
