package hellenicRides.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "QuoteItemOptionCreateDto", description = "Option selection for a quote item.")
public class QuoteItemOptionCreateDto {
  @Schema(description = "Option id", example = "2")
  private Long optionId;

  @Schema(description = "Quantity", example = "1")
  private Integer quantity;
}
