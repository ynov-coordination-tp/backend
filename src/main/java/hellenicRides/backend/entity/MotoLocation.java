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
@Table(name = "moto_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "MotoLocation", description = "Moto inventory item for a category.")
public class MotoLocation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "moto_category_id", nullable = false)
  @Schema(description = "Moto category")
  private MotoCategory motoCategory;

  @Column(nullable = false)
  @Schema(description = "Brand", example = "Yamaha")
  private String brand;

  @Column(nullable = false)
  @Schema(description = "Model", example = "MT-07")
  private String model;

  @Column(nullable = false)
  @Schema(description = "Available units", example = "5")
  private Integer count;
}
