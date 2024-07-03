package com.recruiter.auth_service.service;

import com.recruiter.auth_service.model.Role;
import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.model.request.AuthenticationRequest;
import com.recruiter.auth_service.model.request.RegisterRequest;
import com.recruiter.auth_service.model.response.AuthenticationResponse;
import com.recruiter.auth_service.model.response.RegisterResponse;
import com.recruiter.auth_service.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationService {
    // OTP expiry time in seconds
    private static final long OTP_EXPIRY_SECONDS = 30;
    private final Map<String, LocalDateTime> otpMap = new ConcurrentHashMap<>();


    // dependencies
    private final UserRepository userRepository;

    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender javaMailSender;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(JavaMailSender javaMailSender, UserRepository repository, StorageService storageService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.javaMailSender = javaMailSender;
        this.userRepository = repository;
        this.storageService = storageService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Register a new user
    public RegisterResponse register(RegisterRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            return new RegisterResponse(null, "Username already exists.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return new RegisterResponse(null, "Email already exists.");
        }
        try {
            // Generate OTP
            String otp = generateOTP();

            // Create new user
            User user = new User();
            user.setFirstName(request.getFirstname());
            user.setLastName(request.getLastname());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setStatus(false);
            user.setSubscription(false);
            user.setRole(request.getRole());
            user.setRegistrationDate(LocalDateTime.now());

            if (Role.USER.equals(request.getRole())) {
                user.setOtp(otp);
                sendOTPEmail(user.getEmail(), otp); // Send OTP email
            }
            // Save user and store OTP
            user = userRepository.save(user);
            otpMap.put(otp, LocalDateTime.now());

            // Generate JWT token
            String jwtToken = jwtService.generateToken(user);
            return new RegisterResponse(jwtToken, "User registered successfully.");
        } catch (Exception e) {
            // Handle any unexpected exceptions
            e.printStackTrace();
            return new RegisterResponse(null, "Registration failed. Please try again.");
        }
    }

    // Authenticate a user
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Authenticate user credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            // Fetch user details
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            if (!user.isStatus()) {
                // User is blocked or inactive
                return new AuthenticationResponse(null, null, "User is blocked", null,null,null);
            }
            // Generate JWT token
            String jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponse(user.getId(), jwtToken, "User logged in successfully", user.getRole(),user.getEmail(),user.getSubscription());
        } catch (AuthenticationException e) {
            // authentication failure
            return new AuthenticationResponse(null, null, "Invalid username or password", null,null,null);
        } catch (Exception e) {
            // unexpected exceptions
            e.printStackTrace();
            return new AuthenticationResponse(null, null, "Authentication failed. Please try again.", null,null,null);
        }
    }

    // Generate a random 4-digit OTP
    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            otp.append((int) (Math.random() * 10));
        }
        return otp.toString();
    }

    // Send OTP email
    public void sendOTPEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("marjunramesh@gmail.com"); // From email address
            message.setTo(to);
            message.setSubject("OTP Verification");
            message.setText("Your OTP for registration is: " + otp);
            javaMailSender.send(message);
            System.out.println("Sent OTP email to: " + to);
        } catch (Exception e) {
            // email sending failure
            e.printStackTrace();
            System.out.println("Failed to send OTP email to: " + to);
        }
    }

    public void resendOTP(String email) {
        try {
            // Fetch the user by email from the repository
            User user = userRepository.findByEmail(email);
            if (user != null) {
                // Generate a new OTP
                String newOTP = generateOTP();
                // Set the new OTP to the user object
                user.setOtp(newOTP);
                // Save the updated user object in the repository
                userRepository.save(user);
                // Send the OTP via email
                sendOTPEmail(user.getEmail(), newOTP);
                // Store the OTP generation time in the otpMap
                otpMap.put(newOTP, LocalDateTime.now());
            } else {
                // Handle case where user is not found
                throw new Exception("User not found.");
            }
        } catch (Exception e) {
            // Log the exception and handle it accordingly
            System.err.println("Error while resending OTP: " + e.getMessage());
        }
    }

    public String verifyOTP(String email, String otp) {
        try {
            // Fetch the user by email from the repository
            User user = userRepository.findByEmail(email);
            if (user != null) {
                // Retrieve the stored OTP for the user
                String storedOtp = user.getOtp();
                if (storedOtp != null && storedOtp.trim().equals(otp.trim())) {
                    // Get the OTP creation time from the otpMap
                    LocalDateTime otpCreationTime = otpMap.get(otp);
                    if (otpCreationTime != null && LocalDateTime.now().isBefore(otpCreationTime.plus(OTP_EXPIRY_SECONDS, ChronoUnit.SECONDS))) {
                        // OTP is valid and not expired
                        user.setStatus(true);
                        userRepository.save(user);
                        return "OTP verified successfully.";
                    } else {
                        return "OTP has expired.";
                    }
                } else {
                    return "Invalid OTP.";
                }
            } else {
                return "User not found.";
            }
        } catch (Exception e) {
            // Log the exception and handle it accordingly
            System.err.println("Error while verifying OTP: " + e.getMessage());
            return "An error occurred while verifying OTP.";
        }
    }

    public String verifyDoc(MultipartFile file, String email) {
        try {
            if (file.isEmpty()) {
                return "No file was uploaded.";
            }
            String url = storageService.uploadVerificationDoc(file);
            User user = userRepository.findByEmail(email);
            user.setIdImageUrl(url);
            userRepository.save(user);

            return "File uploaded successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    public String addPicture(MultipartFile file, Integer id) {
        try {
            if (file.isEmpty()) {
                return "No file was uploaded.";
            }
            String url = storageService.uploadVerificationDoc(file);
            User user = userRepository.findById(id).orElseThrow();
            user.setProfileImageUrl(url);
            userRepository.save(user);

            return "File uploaded successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    public String getProfileImage(Integer id) {
        try {
            // Fetch user from repository
            User user = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));

            // Return profile image URL
            return user.getProfileImageUrl();
        } catch (Exception e) {
            // Log the exception and handle it accordingly
            e.printStackTrace();
            return "Failed to retrieve profile image";
        }
    }
}
