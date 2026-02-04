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
@Table(name = "moto_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "MotoCategory", description = "Moto category definition.")
public class MotoCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Schema(description = "Category name", example = "Roadster")
  private String name;
}
