package com.systemmanagement.controller.api;

import com.systemmanagement.dto.ApiResponse;
import com.systemmanagement.dto.UserDTO;
import com.systemmanagement.model.User;
import com.systemmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserRestController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", createdUser));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id,
                                                           @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
    
    // @GetMapping("/search")
    // public ResponseEntity<ApiResponse<List<UserDTO>>> searchUsers(@RequestParam String keyword) {
    //     List<UserDTO> users = userService.searchUsers(keyword);
    //     return ResponseEntity.ok(ApiResponse.success(users));
    // }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByStatus(@PathVariable User.UserStatus status) {
        List<UserDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}