package com.systemmanagement.util;

import com.systemmanagement.model.User;
import com.systemmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasswordMigration implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // migratePasswords();
    }
    
    private void migratePasswords() {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            String currentPassword = user.getPassword();
            
            // Check if password is already encrypted (BCrypt hashes start with $2a$)
            if (!currentPassword.startsWith("$2a$")) {
                // Plain text password - encrypt it
                String encryptedPassword = passwordEncoder.encode(currentPassword);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                
                System.out.println("Encrypted password for user: " + user.getUsername());
            }
        }
        
        System.out.println("Password migration completed!");
    }
}