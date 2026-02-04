package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "formulas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Formula", description = "Tour formula definition.")
public class Formula {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false)
  @Schema(description = "Formula name", example = "Zeus")
  private String name;

  @Column(name = "includes_moto")
  @Schema(description = "Whether the formula includes moto", example = "true")
  private Boolean includesMoto;

  @Column(name = "includes_accommodation")
  @Schema(description = "Whether the formula includes accommodation", example = "true")
  private Boolean includesAccommodation;

  @Column(name = "includes_meals")
  @Schema(description = "Whether the formula includes meals", example = "false")
  private Boolean includesMeals;
}
