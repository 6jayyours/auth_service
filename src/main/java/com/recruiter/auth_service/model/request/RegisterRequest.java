package com.recruiter.auth_service.model.request;

import com.recruiter.auth_service.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private Role role;
}
