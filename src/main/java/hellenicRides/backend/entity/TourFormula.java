package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tour_formulas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "TourFormula", description = "Link between a tour and a formula.")
public class TourFormula {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tour_id", nullable = false)
  @Schema(description = "Tour")
  private Tour tour;

  @ManyToOne
  @JoinColumn(name = "formula_id", nullable = false)
  @Schema(description = "Formula")
  private Formula formula;

  @Column(name = "is_active")
  @Schema(description = "Whether this formula is active for the tour", example = "true")
  private Boolean isActive;
}
