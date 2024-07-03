package com.recruiter.auth_service.model.resume;

import lombok.Data;

@Data
public class ExperienceRequest {
    private String companyName;
    private String role;
    private String startDate;
    private String endDate;
    private int id;
    private int UserId;
}
