package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accommodation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Accommodation", description = "Accommodation details.")
public class Accommodation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false)
  @Schema(description = "Accommodation name", example = "Athens Grand Comfort")
  private String name;

  @Column
  @Schema(description = "City", example = "Athens")
  private String city;

  @Column
  @Schema(description = "Country", example = "Greece")
  private String country;

  @Column
  @Schema(description = "Accommodation type", example = "HOTEL")
  private String type; // HOTEL, AIRBNB, GUESTHOUSE, etc.
}
