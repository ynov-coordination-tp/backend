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

  @Schema(
      description = "Participant name",
      example = "John Doe",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String participantName;

  @Schema(
      description = "Moto location id (nullable for Poseidon formula when using own bike)",
      example = "5")
  private Long motoLocationId;

  @Schema(description = "Accommodation id (nullable for Athena formula)", example = "3")
  private Long accommodationId;

  @Schema(
      description = "Room type (SINGLE, COUPLE, SHARED)",
      example = "SINGLE",
      allowableValues = {"SINGLE", "COUPLE", "SHARED"})
  private String roomType;

  @Schema(description = "Selected options for this participant")
  private List<QuoteItemOptionCreateDto> options;
}
