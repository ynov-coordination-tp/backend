package hellenicRides.backend.dto;

import hellenicRides.backend.entity.QuoteItemOption;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteItemOptionResponseDto {
  private Long id;
  private Long quoteItemId;
  private Long optionId;
  private Integer quantity;
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
