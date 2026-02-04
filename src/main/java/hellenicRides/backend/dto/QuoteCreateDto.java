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

  @Schema(description = "Customer information for quote creation")
  private CustomerDto customer;

  @Schema(
      description = "Tour package id",
      example = "1",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long tourPackageId;

  @Schema(
      description = "Formula id (Zeus=1, Poseidon=2, Athena=3)",
      example = "1",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long formulaId;

  @Schema(
      description = "Departure date",
      example = "2026-06-01",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate departureDate;

  @Schema(
      description = "Return date",
      example = "2026-06-10",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate returnDate;

  @Schema(description = "Quote items (participants)", requiredMode = Schema.RequiredMode.REQUIRED)
  private List<QuoteItemCreateDto> items;
}
