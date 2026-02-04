package hellenicRides.backend.dto;

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
public class QuoteCreateDto {
  private Long customerId;
  private Long tourPackageId;

  private Long formulaId; // Zeus, Poseidon ou Athena

  private LocalDate departureDate;
  private LocalDate returnDate;
  private List<QuoteItemCreateDto> items;
}
