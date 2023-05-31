package com.example.springboot_securitydemo.controller;

import com.example.springboot_securitydemo.models.Role;
import com.example.springboot_securitydemo.models.TestEntity;
import com.example.springboot_securitydemo.models.UserDTO;
import com.example.springboot_securitydemo.security.UserDetailsImpl;
import com.example.springboot_securitydemo.service.RoleService;
import com.example.springboot_securitydemo.util.ServiceLay;
import com.example.springboot_securitydemo.util.IncorrectUserDataException;
import com.example.springboot_securitydemo.util.UserErrorResponse;
import com.example.springboot_securitydemo.util.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.example.springboot_securitydemo.models.User;
import com.example.springboot_securitydemo.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
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
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PatchMapping()
    public ResponseEntity<HttpStatus> editUser( @RequestBody @Valid UserDTO updatedUser, BindingResult result) {
        if ( userService.findUserByName(updatedUser.getUser().getName()).isPresent()
                && !Objects.equals(
                        userService.findUserByName(updatedUser.getUser().getName()).get().getId(),
                        updatedUser.getUser().getId()) ) {
            throw new UserAlreadyExistsException();
        }
        if(updatedUser.getUser().getPassword() == null){
            updatedUser.getUser().setPassword(userService.getUserById(updatedUser.getUser().getId()).getPassword());
        }
        if (!result.hasErrors()) {
            if (!roleService.findByRolename(updatedUser.getRoleName()).isPresent()) {
                updatedUser.getUser().setRoles(Collections.singleton(roleService.findByRolename(updatedUser.getRoleName()).get()));
            } else {
                updatedUser.getUser().setRoles(Collections.singleton(roleService.findByRolename(updatedUser.getRoleName()).get()));
            }
            userService.updateUser(updatedUser.getUser(),updatedUser.getUser().getId() );
        }
        return ResponseEntity.ok(HttpStatus.OK); /// тут не должно быть ОК
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO newUser, BindingResult result) {
        if (userService.findUserByName(newUser.getUser().getName()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for(FieldError error: result.getFieldErrors()) {
                errors.append(error.getField()).append(" - ")
                                                .append(error.getDefaultMessage())
                                                .append( " ; ");
            }
            throw new IncorrectUserDataException(errors.toString());
        }
        newUser.getUser().setRoles(
                Collections.singleton( roleService.findByRolename(newUser.getRoleName() )
                        .get() )
        );
        userService.saveUser(newUser.getUser());
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping("/current")
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public User getCurrent() {
        return userService.getUserById(
                ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUser().getId()
        );
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/users")
    public List<User> getUsers() {

        return userService.getUsers();
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }



//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteUserById(@RequestBody ServiceLay lay) {
        userService.deleteUserById(lay.getUserId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String adminPage(Model model) {
        StringBuilder names = new StringBuilder();
        userService.getUsers().forEach( user -> names.append(user.getName()).append(" ") );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("users", userService.getUsers());
//        model.addAttribute("user", ((UserDetailsImpl) authentication.getPrincipal()).getUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("newRole", new Role());
        model.addAttribute("usernames",names.toString());
        model.addAttribute("service", userService);
        return "admininfo";
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleNotCreatedException(UserAlreadyExistsException e) {
        UserErrorResponse response = new UserErrorResponse(
                "User with this name already exists",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleIncorrectDataException(IncorrectUserDataException e) {
        UserErrorResponse response = new UserErrorResponse(
                "Incorrect user data" + e.getMessages(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
    @ResponseBody
    @PostMapping("/test")
    public ResponseEntity<HttpStatus> test(@RequestBody TestEntity entity) {
        System.out.println(entity.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
