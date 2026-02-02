package hellenicRides.backend.config;

import hellenicRides.backend.entity.Admin;
import hellenicRides.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (adminRepository.findByEmail("admin@test.com") == null) {
                Admin admin = Admin.builder()
                        .email("admin@test.com")
                        .password(passwordEncoder.encode("password"))
                        .build();
                adminRepository.save(admin);
                System.out.println("Default Admin created: admin@test.com / password");
            }
        };
    }
}
