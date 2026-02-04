package hellenicRides.backend.dto;

import hellenicRides.backend.entity.QuoteItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "QuoteItemResponseDto",
    description = "Quote item details returned in a quote response.")
public class QuoteItemResponseDto {
  @Schema(description = "Unique identifier", example = "1")
  private Long id;

  @Schema(description = "Quote id", example = "10")
  private Long quoteId;

  @Schema(description = "Participant name", example = "John Doe")
  private String participantName;

  @Schema(description = "Moto location id", example = "5")
  private Long motoLocationId;

  @Schema(description = "Accommodation id", example = "3")
  private Long accommodationId;

  @Schema(description = "Locked unit price", example = "1200.00")
  private BigDecimal lockedUnitPrice;

  @Schema(description = "Selected options")
  private List<QuoteItemOptionResponseDto> options;

  public static QuoteItemResponseDto fromEntity(
      QuoteItem item, List<QuoteItemOptionResponseDto> options) {
    return QuoteItemResponseDto.builder()
        .id(item.getId())
        .quoteId(item.getQuoteId())
        .participantName(item.getParticipantName())
        .motoLocationId(item.getMotoLocationId())
        .accommodationId(item.getAccommodationId())
        .lockedUnitPrice(item.getLockedUnitPrice())
        .options(options)
        .build();
  }
}
