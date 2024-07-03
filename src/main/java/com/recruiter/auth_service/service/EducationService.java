package com.recruiter.auth_service.service;


import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.model.resume.Education;
import com.recruiter.auth_service.model.resume.EducationRequest;
import com.recruiter.auth_service.repository.EducationRepository;
import com.recruiter.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    public EducationService(EducationRepository educationRepository, UserRepository userRepository) {
        this.educationRepository = educationRepository;
        this.userRepository = userRepository;
    }

    public String addUserEducation(EducationRequest request) {
        try {

            Integer userId = request.getUserId();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            Education education = new Education();
            education.setCollegeName(request.getCollegeName());
            education.setUser(user);
            education.setPlace(request.getPlace());
            education.setYear(request.getYear());
            education.setDegree(request.getDegree());

            educationRepository.save(education);

            return "success";
        }  catch (Exception e) {
            // Handle any other exceptions
            return "Error saving education: " + e.getMessage();
        }
    }


    public List<Education> getUserEducation(Integer userId) {
        return educationRepository.findEducationByUserId(userId);
    }

    public Optional<Education> getEducation(int id) {
        return educationRepository.findById(id);
    }

    public String updateUserEducation(int id, EducationRequest request) {
        Education education = educationRepository.findById(id).orElseThrow();
        education.setCollegeName(request.getCollegeName());
        education.setDegree(request.getDegree());
        education.setYear(request.getYear());
        education.setPlace(request.getPlace());
        educationRepository.save(education);
        return "education edited successfully";
    }
}
