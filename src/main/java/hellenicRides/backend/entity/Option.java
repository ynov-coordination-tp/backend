package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Option", description = "Extra option for quotes or items.")
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false)
  @Schema(description = "Option name", example = "Airport transfer")
  private String name;

  @Column(precision = 10, scale = 2)
  @Schema(description = "Option price", example = "50.00")
  private BigDecimal price;

  @Column(name = "target_type")
  @Schema(description = "Target type", example = "QUOTE_ITEM")
  private String targetType; // QUOTE_ITEM, QUOTE
}
