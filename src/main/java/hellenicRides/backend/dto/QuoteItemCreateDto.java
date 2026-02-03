package hellenicRides.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteItemCreateDto {
  private String participantName;
  private Long motoLocationId; // nullable for Poseidon formula
  private Long accommodationId;
  private List<QuoteItemOptionCreateDto> options;
}
