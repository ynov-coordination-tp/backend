package hellenicRides.backend.service;

import hellenicRides.backend.entity.Admin;
import hellenicRides.backend.entity.AdminPost;
import hellenicRides.backend.repository.AdminRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCrudService {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  public List<Admin> getAllAdmins() {
    return adminRepository.findAll();
  }

  public Optional<Admin> getAdminById(Long id) {
    return adminRepository.findById(id);
  }

  public Admin createAdmin(AdminPost adminPost) {
    Admin admin =
        Admin.builder()
            .email(adminPost.getEmail())
            .password(passwordEncoder.encode(adminPost.getPassword()))
            .build();
    return adminRepository.save(admin);
  }

  public Optional<Admin> updateAdmin(Long id, AdminPost adminPost) {
    return adminRepository
        .findById(id)
        .map(
            existing -> {
              if (adminPost.getEmail() != null) {
                existing.setEmail(adminPost.getEmail());
              }
              if (adminPost.getPassword() != null) {
                existing.setPassword(passwordEncoder.encode(adminPost.getPassword()));
              }
              return adminRepository.save(existing);
            });
  }

  public boolean deleteAdmin(Long id) {
    if (!adminRepository.existsById(id)) {
      return false;
    }
    adminRepository.deleteById(id);
    return true;
  }
}
