package com.example.springboot_securitydemo.controller;

import com.example.springboot_securitydemo.security.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public String getInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user",((UserDetailsImpl) authentication.getPrincipal()).getUser());
        return "showinfo";
    }
}
