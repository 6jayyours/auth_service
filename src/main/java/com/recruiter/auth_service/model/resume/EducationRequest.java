package com.recruiter.auth_service.model.resume;


import lombok.Data;

@Data
public class EducationRequest {
    private String collegeName;
    private String year;
    private String degree;
    private String place;
    private int educationId;
    private int userId;
}
