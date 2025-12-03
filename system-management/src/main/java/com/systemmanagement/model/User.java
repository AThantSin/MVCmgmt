package com.systemmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username is required")  
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")  
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank(message = "Email is required")  
    @Email(message = "Email must be valid")  
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank(message = "Password is required")  
    @Size(min = 6, message = "Password must be at least 6 characters")  
    @Column(nullable = false)
    private String password;
    
    @NotBlank(message = "Full name is required")  
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")  
    private String fullName;
    
    @Pattern(regexp = "^[0-9-+()\\s]*$", message = "Phone number can only contain numbers, spaces, and symbols")  
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}