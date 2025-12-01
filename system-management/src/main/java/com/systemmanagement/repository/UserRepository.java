package com.systemmanagement.repository;

import com.systemmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    // Optional<User> findByPhoneNumber(String phoneNumber); Need for Example: SMS verification, phone-based login
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    // boolean existsByPhoneNumber(String phoneNumber);
    
    List<User> findByStatus(User.UserStatus status);
    
    @Query("SELECT u FROM User u WHERE " +
       "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "+
       "LOWER(u.status) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
}