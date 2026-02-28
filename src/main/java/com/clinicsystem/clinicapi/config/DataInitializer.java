package com.clinicsystem.clinicapi.config;

import com.clinicsystem.clinicapi.model.Role;
import com.clinicsystem.clinicapi.model.User;
import com.clinicsystem.clinicapi.repository.RoleRepository;
import com.clinicsystem.clinicapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // Initialize roles if they don't exist
            for (Role.RoleName roleName : Role.RoleName.values()) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                    log.info("Created role: {}", roleName);
                }
            }

            // Create admin user if not exists
            if (userRepository.findByEmail("admin@clinic.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@clinic.com");
                admin.setPasswordHash(passwordEncoder.encode("admin123")); // BCrypt encoded
                admin.setPhone("0123456789");
                admin.setFullName("Admin User");
                admin.setRole(Role.RoleName.ROLE_ADMIN);
                admin.setStatus(User.UserStatus.active);
                admin.setEmailVerified(true);
                admin.setPhoneVerified(true);
                userRepository.save(admin);
                log.info("Created admin user: admin@clinic.com / admin123");
            }

            log.info("Data initialization completed");
        };
    }
}
