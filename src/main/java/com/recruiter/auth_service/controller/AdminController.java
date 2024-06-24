package com.recruiter.auth_service.controller;

import com.recruiter.auth_service.model.Role;
import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.service.AuthenticationService;
import com.recruiter.auth_service.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class AdminController {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationService authenticationService;

    public AdminController(UserDetailsServiceImpl userDetailsService, AuthenticationService authenticationService) {
        this.userDetailsService = userDetailsService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam Role role) {
        List<User> users = userDetailsService.getUsersByRole(role);
        System.out.println();
        return ResponseEntity.ok(users);
    }

}
