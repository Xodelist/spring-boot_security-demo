package com.example.springboot_securitydemo.models;

import javax.validation.Valid;

public class UserDTO {

    @Valid
    private User user;
    private String roleName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
