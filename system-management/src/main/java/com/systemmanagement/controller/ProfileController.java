package com.systemmanagement.controller;

import com.systemmanagement.dto.UserDTO;
import com.systemmanagement.model.User;
import com.systemmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    
    private final UserRepository userRepository;
    
    @GetMapping("/profile")
    public String viewProfile(Model model) {
        // Get currently logged-in username from Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Get user details from database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        return "profile";
    }
}