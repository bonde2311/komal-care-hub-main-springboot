package com.komal.carehub.config;

import com.komal.carehub.entity.User;
import com.komal.carehub.entity.enums.Role;
import com.komal.carehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@komalmedical.com";
        Optional<User> admin = userRepository.findByEmail(adminEmail);

        if (admin.isEmpty()) {
            User newAdmin = User.builder()
                    .name("Store Admin")
                    .email(adminEmail)
                    .phone("0000000000")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(newAdmin);
            System.out.println("✅ Default Admin User Created: " + adminEmail + " / admin123");
        }
    }
}
