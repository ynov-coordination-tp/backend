package hellenicRides.backend.controller;

import hellenicRides.backend.dto.AuthResponse;
import hellenicRides.backend.entity.AdminPost;
import hellenicRides.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AdminService adminService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AdminPost admin)
      throws Exception {
    return ResponseEntity.ok(adminService.login(admin));
  }
}
