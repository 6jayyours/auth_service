package com.recruiter.auth_service.controller;

import com.recruiter.auth_service.model.Role;
import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.model.resume.*;
import com.recruiter.auth_service.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationService authenticationService;

    private final EducationService educationService;
    private final ExperienceService experienceService;
    private final SkillsService skillsService;

    public UserController(UserDetailsServiceImpl userDetailsService, AuthenticationService authenticationService, EducationService educationService, ExperienceService experienceService, SkillsService skillsService) {
        this.userDetailsService = userDetailsService;
        this.authenticationService = authenticationService;
        this.educationService = educationService;
        this.experienceService = experienceService;
        this.skillsService = skillsService;
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

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable Integer id){
        return ResponseEntity.ok(userDetailsService.updateUserStatus(id));
    }

    @PostMapping("/addExperience")
    public ResponseEntity<String> addExperience(@RequestBody ExperienceRequest request) {
        String result = experienceService.addUserExperience(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getExperience/{userId}")
    public ResponseEntity<List> getUserExperience(@PathVariable Integer userId) {
        List<Experience> experience = experienceService.getUserExperience(userId);
        return ResponseEntity.ok(experience);
    }

    @PostMapping("/addEducation")
    public ResponseEntity<String> addEducation(@RequestBody EducationRequest request) {
        String result = educationService.addUserEducation(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getEducation/{userId}")
    public ResponseEntity<List> getUserEducation(@PathVariable Integer userId) {
        List<Education> education = educationService.getUserEducation(userId);
        return ResponseEntity.ok(education);
    }

    @PostMapping("/addSkill")
    public ResponseEntity<String> addSkill(@RequestBody SkillRequest request) {
        String result = skillsService.addUserSkill(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getSkill/{userId}")
    public ResponseEntity<List> getUserSkill(@PathVariable Integer userId) {
        List<Skills> skills = skillsService.getUserSkills(userId);
        return ResponseEntity.ok(skills);
    }
}
