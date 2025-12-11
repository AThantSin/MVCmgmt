package com.systemmanagement.service;

import com.systemmanagement.model.PasswordResetToken;
import com.systemmanagement.model.User;
import com.systemmanagement.repository.PasswordResetTokenRepository;
import com.systemmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final int EXPIRATION_HOURS = 24;
    
    @Transactional
    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        
        // Delete any existing tokens for this user
        tokenRepository.deleteByUser(user);
        
        // Generate unique token
        String token = UUID.randomUUID().toString();
        
        // Create reset token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(EXPIRATION_HOURS));
        resetToken.setUsed(false);
        
        tokenRepository.save(resetToken);
        
        return token;
    }
    
    public boolean validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElse(null);
        
        if (resetToken == null) {
            return false;
        }
        
        if (resetToken.isExpired()) {
            return false;
        }
        
        if (resetToken.isUsed()) {
            return false;
        }
        
        return true;
    }
    
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        
        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token has expired");
        }
        
        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("Token already used");
        }
        
        // Update user password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}