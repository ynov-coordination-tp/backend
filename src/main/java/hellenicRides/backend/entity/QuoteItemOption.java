package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quote_item_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "QuoteItemOption", description = "Option attached to a quote item.")
public class QuoteItemOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(name = "quote_item_id", nullable = false)
  @Schema(description = "Quote item id", example = "10")
  private Long quoteItemId;

  @Column(name = "option_id", nullable = false)
  @Schema(description = "Option id", example = "2")
  private Long optionId;

  @Column
  @Schema(description = "Quantity", example = "2")
  private Integer quantity;

  @Column(name = "locked_price", precision = 10, scale = 2)
  @Schema(
      description = "Locked price",
      example = "150.00",
      accessMode = Schema.AccessMode.READ_ONLY)
  private BigDecimal lockedPrice;
}
