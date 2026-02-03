package hellenicRides.backend.dto;

import hellenicRides.backend.entity.Quote;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteResponseDto {
  private Long id;
  private String quoteNumber;
  private LocalDateTime createdAt;
  private LocalDate departureDate;
  private LocalDate returnDate;
  private Long customerId;
  private Long tourPackageId;
  private Integer participantCount;
  private BigDecimal lockedTotalPrice;
  private String status;
  private List<QuoteItemResponseDto> items;

  public static QuoteResponseDto fromEntity(Quote quote, List<QuoteItemResponseDto> items) {
    return QuoteResponseDto.builder()
        .id(quote.getId())
        .quoteNumber(quote.getQuoteNumber())
        .createdAt(quote.getCreatedAt())
        .departureDate(quote.getDepartureDate())
        .returnDate(quote.getReturnDate())
        .customerId(quote.getCustomerId())
        .tourPackageId(quote.getTourPackageId())
        .participantCount(quote.getParticipantCount())
        .lockedTotalPrice(quote.getLockedTotalPrice())
        .status(quote.getStatus())
        .items(items)
        .build();
  }
}
