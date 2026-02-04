package hellenicRides.backend.dto;

import hellenicRides.backend.entity.QuoteItemOption;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "QuoteItemOptionResponseDto",
    description = "Option details returned in a quote item response.")
public class QuoteItemOptionResponseDto {
  @Schema(description = "Unique identifier", example = "1")
  private Long id;

  @Schema(description = "Quote item id", example = "10")
  private Long quoteItemId;

  @Schema(description = "Option id", example = "2")
  private Long optionId;

  @Schema(description = "Quantity", example = "1")
  private Integer quantity;

  @Schema(description = "Locked price", example = "150.00")
  private BigDecimal lockedPrice;

  public static QuoteItemOptionResponseDto fromEntity(QuoteItemOption option) {
    return QuoteItemOptionResponseDto.builder()
        .id(option.getId())
        .quoteItemId(option.getQuoteItemId())
        .optionId(option.getOptionId())
        .quantity(option.getQuantity())
        .lockedPrice(option.getLockedPrice())
        .build();
  }
}
