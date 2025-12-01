package com.systemmanagement.service;

import com.systemmanagement.dto.UserDTO;
import com.systemmanagement.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    
    List<UserDTO> getAllUsers();
    
    UserDTO getUserById(Long id);
    
    UserDTO createUser(UserDTO userDTO);
    
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    void deleteUser(Long id);
    
    List<UserDTO> getUsersByStatus(User.UserStatus status);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    Page<UserDTO> getAllUsers(Pageable pageable);

    Page<UserDTO> searchUsers(String keyword, Pageable pageable);
}