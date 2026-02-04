package hellenicRides.backend.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CustomerDto {
  private Long id;
  private String lastName;
  private String firstName;

  @Email private String email;
  private String phone;
}
