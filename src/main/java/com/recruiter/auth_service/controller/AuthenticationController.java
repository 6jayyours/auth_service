package com.recruiter.auth_service.controller;

import com.recruiter.auth_service.model.request.AuthenticationRequest;
import com.recruiter.auth_service.model.request.RegisterRequest;
import com.recruiter.auth_service.model.response.AuthenticationResponse;
import com.recruiter.auth_service.model.response.RegisterResponse;
import com.recruiter.auth_service.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/resendOTP")
    public ResponseEntity<String> resendOTP(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        authenticationService.resendOTP(email);
        return ResponseEntity.ok("OTP resent successfully");
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String otp = requestBody.get("otp");
        String verificationResult = authenticationService.verifyOTP(email, otp);
        return ResponseEntity.ok(verificationResult);
    }

    @PostMapping("/verifyDoc")
    public ResponseEntity<String> verifyDoc(@RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
        return ResponseEntity.ok(authenticationService.verifyDoc(file, email));
    }

}
