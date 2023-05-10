package com.example.springboot_securitydemo.controller;

import com.example.springboot_securitydemo.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.springboot_securitydemo.models.User;
import com.example.springboot_securitydemo.service.UserService;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService service;

    @Autowired
    public AdminController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", service.getUserById(id));
        return "show";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", service.getUsers());
        return "index";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editUserPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", service.getUserById(id));
        model.addAttribute("role", new Role());
        return "edit";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/edit")
    public String editUser(@ModelAttribute("user") @Valid User user, BindingResult result,@PathVariable("id") int id) {
        if (!result.hasErrors()) {
            Set<Role> currentRoles = service.getUserById(id).getRoles();
            user.setRoles(currentRoles);
            service.updateUser(user, id);
            return "redirect:/admin";
        } else {
            return "edit";
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String saveUserPage(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (!result.hasErrors()) {
            service.saveUser(user);
            return "redirect:/admin";
        } else {
            return "new";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        service.deleteUserById(id);
        return "redirect:/admin";
    }
}
