package com.recruiter.auth_service.controller;

import com.recruiter.auth_service.model.Role;
import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class AdminController {

    private final UserDetailsServiceImpl userDetailsService;

    public AdminController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam Role role) {
        List<User> users = userDetailsService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
}
