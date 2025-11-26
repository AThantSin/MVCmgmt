package com.systemmanagement.service;

import com.systemmanagement.dto.UserDTO;
import com.systemmanagement.model.User;
import java.util.List;

public interface UserService {
    
    List<UserDTO> getAllUsers();
    
    UserDTO getUserById(Long id);
    
    UserDTO createUser(UserDTO userDTO);
    
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    void deleteUser(Long id);
    
    List<UserDTO> searchUsers(String keyword);
    
    List<UserDTO> getUsersByStatus(User.UserStatus status);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}