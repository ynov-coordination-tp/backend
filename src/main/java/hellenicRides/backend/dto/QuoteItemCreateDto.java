package hellenicRides.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "QuoteItemCreateDto", description = "Quote item creation payload.")
public class QuoteItemCreateDto {
  @Schema(description = "Participant name", example = "John Doe")
  private String participantName;

  @Schema(description = "Moto location id (nullable for non-moto formulas)", example = "5")
  private Long motoLocationId; // nullable for Poseidon formula

  @Schema(description = "Accommodation id", example = "3")
  private Long accommodationId;

  @Schema(description = "Selected options")
  private List<QuoteItemOptionCreateDto> options;
}
