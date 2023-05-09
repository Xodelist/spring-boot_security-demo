package com.example.springboot_securitydemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springboot_securitydemo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
