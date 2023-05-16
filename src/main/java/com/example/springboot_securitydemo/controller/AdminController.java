package com.example.springboot_securitydemo.controller;

import com.example.springboot_securitydemo.models.Role;
import com.example.springboot_securitydemo.security.UserDetailsImpl;
import com.example.springboot_securitydemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import com.example.springboot_securitydemo.models.User;
import com.example.springboot_securitydemo.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService service, RoleService roleService) {
        this.userService = service;
        this.roleService = roleService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/edit")
    public String editUser(@ModelAttribute("newRole") Role newRole,@ModelAttribute("newUser") @Valid User newUser, BindingResult result,@PathVariable("id") int id) {
        if (userService.findUserByName(newUser.getName()).isPresent()
                && !Objects.equals(userService.findUserByName(newUser.getName()).get().getId(), newUser.getId())) {
            return "redirect:/admin";
        }
        if(newUser.getPassword().length() < 8 || newUser.getPassword().length() > 50 || newUser.getPassword() == null){
            newUser.setPassword(userService.getUserById(id).getPassword());
        }
        if (!result.hasErrors()) {
            newUser.setRoles(Collections.singleton(roleService.findByRolename(newRole.getRolename()).get()));
            userService.updateUser(newUser, id);
        }
        return "redirect:/admin";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public String saveUser(@ModelAttribute("newRole") Role newRole, @ModelAttribute("newUser") @Valid User newUser, BindingResult result) {
        if (userService.findUserByName(newUser.getName()).isPresent()) {
            return "redirect:/admin";
        }
        if (!result.hasErrors()) {
            newUser.setRoles(Collections.singleton(roleService.findByRolename(newRole.getRolename()).get()));
            userService.saveUser(newUser);
            return "redirect:/admin";
        } else {
            return "redirect:/admin";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String adminPage(Model model) {
        StringBuilder names = new StringBuilder();
        userService.getUsers().forEach( user -> names.append(user.getName()).append(" ") );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("user", ((UserDetailsImpl) authentication.getPrincipal()).getUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("newRole", new Role());
        model.addAttribute("usernames",names.toString());
        model.addAttribute("service", userService);
        return "admininfo";
    }

}
