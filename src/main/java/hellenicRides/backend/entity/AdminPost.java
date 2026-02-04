package hellenicRides.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "AdminPost", description = "Admin login or management payload.")
public class AdminPost {
  @Schema(description = "Admin email address", example = "admin@test.com")
  private String email;

  @Schema(description = "Admin password", example = "password")
  private String password;
}
