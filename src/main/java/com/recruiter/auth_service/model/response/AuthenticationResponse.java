package com.recruiter.auth_service.model.response;

import com.recruiter.auth_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private Integer id;
    private String token;
    private String message;
    private Role role;
    private String email;
    private Boolean subscription;

}
