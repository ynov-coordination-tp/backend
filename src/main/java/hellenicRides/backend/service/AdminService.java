package hellenicRides.backend.service;

import hellenicRides.backend.dto.AuthResponse;
import hellenicRides.backend.entity.AdminPost;
import hellenicRides.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService userDetailsService;
  private final JwtUtil jwtUtil;

  public AuthResponse login(AdminPost adminPost) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(adminPost.getEmail(), adminPost.getPassword()));
    } catch (Exception e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(adminPost.getEmail());

    final String jwt = jwtUtil.generateToken(userDetails);

    return new AuthResponse(jwt);
  }
}
