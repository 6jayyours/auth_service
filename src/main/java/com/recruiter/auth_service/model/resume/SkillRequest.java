package com.recruiter.auth_service.model.resume;

import lombok.Data;

@Data
public class SkillRequest {
    private int userId;
    private String skills;
}
