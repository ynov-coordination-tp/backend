package hellenicRides.backend.entity;

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
public class QuoteItemOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quote_item_id", nullable = false)
  private Long quoteItemId;

  @Column(name = "option_id", nullable = false)
  private Long optionId;

  @Column private Integer quantity;

  @Column(name = "locked_price", precision = 10, scale = 2)
  private BigDecimal lockedPrice;
}
