package com.example.springboot_securitydemo.models;





import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name",unique = true,nullable = false)
    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 3,max = 25, message = "Name size should be between 3 to 25 characters")
    private String name;
    @Column(name = "age")
    @Min(value = 0, message = "Your age can't be less than 0")
    @Max(value = 100, message = "You're too old to be there")
    private Integer age;
    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password shouldn't be empty")
    @Size(min = 8, max = 50, message = "Your password should be between 8 to 50 characters")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns =  @JoinColumn (name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    private Set<Role> roles = new HashSet<>();

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
