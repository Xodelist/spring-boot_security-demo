package com.example.springboot_securitydemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.springboot_securitydemo.models.User;
import com.example.springboot_securitydemo.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public String showUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", service.getUserById(id));
        return "show";
    }
    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", service.getUsers());
        return "index";
    }
    @GetMapping("/{id}/edit")
    public String editUserPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", service.getUserById(id));
        return "edit";
    }
    @PatchMapping("/{id}/edit")
    public String editUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        service.updateUser(user,id);
        return "redirect:/users";
    }
    @GetMapping("/new")
    public String saveUserPage(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }
    @PostMapping
    public String saveUser(@ModelAttribute("user") User user) {
        service.saveUser(user);
        return "redirect:/users";
    }
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        service.deleteUserById(id);
        return "redirect:/users";
    }
}
