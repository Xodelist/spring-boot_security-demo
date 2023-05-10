package com.example.springboot_securitydemo.service;

import com.example.springboot_securitydemo.models.User;
import com.example.springboot_securitydemo.repository.UserRepository;
import com.example.springboot_securitydemo.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public UserDetailServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findUserByName(username);
        if (user.isPresent()) {
            return new UserDetailsImpl(user.get());
        }
        throw new UsernameNotFoundException("User not found");
    }
}
