package com.systemmanagement.controller;

import com.systemmanagement.dto.UserDTO;
import com.systemmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public String listUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user/form";
    }

    @PostMapping
    public String createUser(@ModelAttribute UserDTO user) {
        userService.createUser(user);
        return "redirect:/users";
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
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDTO user) {
        user.setId(id);
        userService.updateUser(id, user);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}