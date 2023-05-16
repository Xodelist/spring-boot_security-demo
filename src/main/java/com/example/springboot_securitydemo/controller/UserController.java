package com.example.springboot_securitydemo.controller;

import com.example.springboot_securitydemo.models.User;
import com.example.springboot_securitydemo.security.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/user")
public class UserController {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public String getInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        model.addAttribute("user",currentUser);
        model.addAttribute("roles", Arrays.toString(currentUser.getRoles().toArray()));
        return "userinfo";
    }
}
