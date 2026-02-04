package hellenicRides.backend.service;

import hellenicRides.backend.dto.AuthResponse;
import hellenicRides.backend.entity.AdminPost;
import hellenicRides.backend.util.JwtUtil;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService userDetailsService;
  private final JwtUtil jwtUtil;
  private final EmailService emailService;

  public AuthResponse login(AdminPost adminPost) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(adminPost.getEmail(), adminPost.getPassword()));
    } catch (Exception e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(adminPost.getEmail());

    final String jwt = jwtUtil.generateToken(userDetails);

    try {
      File f = new File("C:\\Users\\elias\\Downloads\\Cahier des charges - Gemini.pdf");
      emailService.sendEmailWithAttachment(
          "elias.eloudghiri@gmail.com",
          "Admin Login Alert",
          "Admin with email " + adminPost.getEmail() + " has logged in.",
          f);

    } catch (Exception e) {
      log.error("Failed to send admin login alert email.");
      log.error("Error: {}", e.getMessage());
    }

    return new AuthResponse(jwt);
  }
}
