package hellenicRides.backend.controller;

import hellenicRides.backend.dto.AuthResponse;
import hellenicRides.backend.entity.AdminPost;
import hellenicRides.backend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints.")
public class AuthController {

  private final AdminService adminService;

  @PostMapping("/login")
  @Operation(
      summary = "Admin login",
      description = "Public endpoint to authenticate an admin and return a JWT.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Authenticated successfully."),
    @ApiResponse(responseCode = "401", description = "Invalid credentials.")
  })
  public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AdminPost admin)
      throws Exception {
    return ResponseEntity.ok(adminService.login(admin));
  }
}
