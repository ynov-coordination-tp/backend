package hellenicRides.backend.dto;

import hellenicRides.backend.entity.QuoteItem;
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
public class QuoteItemResponseDto {
  private Long id;
  private Long quoteId;
  private String participantName;
  private Long motoLocationId;
  private Long accommodationId;
  private BigDecimal lockedUnitPrice;
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
