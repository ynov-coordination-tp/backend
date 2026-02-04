package hellenicRides.backend.dto;

import hellenicRides.backend.entity.Quote;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "QuoteResponseDto", description = "Quote details with items.")
public class QuoteResponseDto {
  @Schema(description = "Unique identifier", example = "1")
  private Long id;

  @Schema(description = "Quote number", example = "QT-AB12CD34")
  private String quoteNumber;

  @Schema(description = "Creation date time", example = "2026-02-04T10:15:30")
  private LocalDateTime createdAt;

  @Schema(description = "Departure date", example = "2026-06-01")
  private LocalDate departureDate;

  @Schema(description = "Return date", example = "2026-06-10")
  private LocalDate returnDate;

  @Schema(description = "Customer id", example = "100")
  private Long customerId;

  @Schema(description = "Tour package id", example = "200")
  private Long tourPackageId;

  @Schema(description = "Participant count", example = "2")
  private Integer participantCount;

  @Schema(description = "Locked total price", example = "3500.00")
  private BigDecimal lockedTotalPrice;

  @Schema(description = "Status", example = "DRAFT")
  private String status;

  @Schema(description = "Quote items")
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
