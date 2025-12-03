package com.systemmanagement.controller;

import com.systemmanagement.dto.UserDTO;
import com.systemmanagement.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;  


@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userPage = userService.getAllUsers(pageable);
        
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        
        return "user/list";
    }

    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userService.searchUsers(keyword, pageable);
        } else {
            userPage = userService.getAllUsers(pageable);
        }
        
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        
        return "user/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user/form";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, 
            Model model) {  
        
        // Validate password manually for creation
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            result.rejectValue("password", "error.user", "Password is required");
        } else if (user.getPassword().length() < 6) {
            result.rejectValue("password", "error.user", "Password must be at least 6 characters");
        }
        
        if (result.hasErrors()) {  
            return "user/form";  
        }
        
        try {  
            userService.createUser(user);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {  
            model.addAttribute("error", e.getMessage());  
            return "user/form";  
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/form";
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/detail";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") UserDTO user,  
         BindingResult result,Model model) {
        
        // Only validate password if user entered something
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            if (user.getPassword().length() < 6) {
                result.rejectValue("password", "error.user", "Password must be at least 6 characters");
            }
        }

        if (result.hasErrors()) { 
            return "user/form";  
        }
        
        try {  
            user.setId(id);
            userService.updateUser(id, user);
            return "redirect:/users";
        } catch (IllegalArgumentException e) { 
            model.addAttribute("error", e.getMessage());  
            return "user/form";  
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    

}