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
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationService authenticationService;

    public UserController(UserDetailsServiceImpl userDetailsService, AuthenticationService authenticationService) {
        this.userDetailsService = userDetailsService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/candidate")
    public ResponseEntity<User> getUser(@RequestParam Integer id) {
        User user = userDetailsService.getUsersById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam Role role) {
        List<User> users = userDetailsService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/profilePicture")
    public ResponseEntity<String> profilePicture(@RequestParam("file") MultipartFile file, @RequestParam("id") Integer id) {
        return ResponseEntity.ok(authenticationService.addPicture(file, id));
    }

    @GetMapping("/getProfileImage")
    public ResponseEntity<String> getImage(@RequestParam Integer id) {
        return ResponseEntity.ok(authenticationService.getProfileImage(id));
    }


}
