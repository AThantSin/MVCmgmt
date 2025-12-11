package com.systemmanagement.controller;

import com.systemmanagement.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {
    
    private final PasswordResetService passwordResetService;
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        try {
            String token = passwordResetService.createPasswordResetToken(email);
            
            // For development: Show reset link directly
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            model.addAttribute("resetLink", resetLink);
            model.addAttribute("success", true);
            
            // In production: Send email with reset link instead
            // emailService.sendPasswordResetEmail(email, resetLink);
            
            return "forgot-password";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
    }
    
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        if (!passwordResetService.validateToken(token)) {
            model.addAttribute("error", "Invalid or expired reset link");
            return "reset-password";
        }
        
        model.addAttribute("token", token);
        return "reset-password";
    }
    
    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "reset-password";
        }
        
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            model.addAttribute("token", token);
            return "reset-password";
        }
        
        try {
            passwordResetService.resetPassword(token, password);
            model.addAttribute("success", true);
            return "reset-password";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            return "reset-password";
        }
    }
}