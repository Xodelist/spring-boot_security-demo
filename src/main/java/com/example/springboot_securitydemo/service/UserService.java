package com.example.springboot_securitydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springboot_securitydemo.models.User;
import com.example.springboot_securitydemo.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUserById(int id) {
            return repository.getById(id);
    }
    public List<User> getUsers() {
        return repository.findAll();
    }
    @Transactional
    public void updateUser(User updatedUser, int id) {
        updatedUser.setId(id);
        repository.save(updatedUser);
    }
    @Transactional
    public void saveUser(User user) {
        repository.save(user);
    }
    @Transactional
    public void deleteUserById(int id) {
        repository.deleteById(id);
    }

}
