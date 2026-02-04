package hellenicRides.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "QuoteCreateDto", description = "Quote creation payload.")
public class QuoteCreateDto {
  @Schema(description = "Customer id", example = "100")
  private Long customerId;

  @Schema(description = "Tour package id", example = "200")
  private Long tourPackageId;

  @Schema(description = "Departure date", example = "2026-06-01")
  private LocalDate departureDate;

  @Schema(description = "Return date", example = "2026-06-10")
  private LocalDate returnDate;

  @Schema(description = "Quote items")
  private List<QuoteItemCreateDto> items;
}
