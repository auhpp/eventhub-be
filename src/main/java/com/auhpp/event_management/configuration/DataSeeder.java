package com.auhpp.event_management.configuration;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initAdmin(AppUserRepository repo, RoleRepository roleRepository, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail(adminEmail).isEmpty()) {
                AppUser admin = new AppUser();
                admin.setEmail(adminEmail);
                admin.setPassword(encoder.encode(adminPassword));
                admin.setRole(roleRepository.findByName(RoleName.ADMIN.name()));
                admin.setStatus(true);
                repo.save(admin);
            }
        };
    }
}