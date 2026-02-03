package hellenicRides.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "target_type")
  private String targetType; // QUOTE_ITEM, QUOTE
}
