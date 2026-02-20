package com.platform.backend.config;

import com.platform.backend.entity.User;
import com.platform.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);
        
        User existing = userService.getByUsername("admin");
        if (existing == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encodedPassword);
            admin.setEmail("admin@example.com");
            admin.setRole("admin");
            admin.setStatus(1);
            userService.save(admin);
            System.out.println("Admin user created with password: " + rawPassword);
        } else {
            existing.setPassword(encodedPassword);
            userService.updateById(existing);
            System.out.println("Admin password reset to: " + rawPassword);
        }
    }
}
