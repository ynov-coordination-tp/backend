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
@Table(name = "tours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Tour", description = "Tour description and metadata.")
public class Tour {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false)
  @Schema(description = "Tour name", example = "Greece Escape")
  private String name;

  @Column(nullable = false)
  @Schema(description = "Country", example = "Greece")
  private String country;

  @Column(name = "duration_days")
  @Schema(description = "Duration in days", example = "10")
  private Integer durationDays;

  @Column(columnDefinition = "TEXT")
  @Schema(description = "Description", example = "A scenic tour across Greece.")
  private String description;

  @Column(name = "distance_km")
  private Integer distanceKm;
}
