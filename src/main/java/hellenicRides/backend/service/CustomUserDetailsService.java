package hellenicRides.backend.service;

import hellenicRides.backend.entity.Admin;
import hellenicRides.backend.repository.AdminRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Admin admin = adminRepository.findByEmail(email);

    if (admin == null) {
      throw new UsernameNotFoundException("Admin not found with email: " + email);
    }

    // We can add roles here if needed, for now just empty list
    return new User(admin.getEmail(), admin.getPassword(), new ArrayList<>());
  }
}
