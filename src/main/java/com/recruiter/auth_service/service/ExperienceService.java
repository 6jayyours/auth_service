package com.recruiter.auth_service.service;


import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.model.resume.Experience;
import com.recruiter.auth_service.model.resume.ExperienceRequest;
import com.recruiter.auth_service.repository.ExperienceRepository;
import com.recruiter.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;

    public ExperienceService(ExperienceRepository experienceRepository, UserRepository userRepository) {
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
    }

    public String addUserExperience(ExperienceRequest request) {
        try {
            Integer userId = request.getUserId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Experience experience = new Experience();
            experience.setUser(user);
            experience.setRole(request.getRole());
            experience.setCompanyName(request.getCompanyName());
            experience.setStartDate(LocalDate.parse(request.getStartDate()));
            experience.setEndDate(LocalDate.parse(request.getEndDate()));

            experienceRepository.save(experience);

            return "success";
        } catch (RuntimeException e) {
            // Handle the exception, log it, or rethrow it if needed
            return "error: " + e.getMessage();
        }
    }


    public List<Experience> getUserExperience(Integer userId) {
        return experienceRepository.findExperienceByUserId(userId);
    }

    public Optional<Experience> getExperience(int id) {
        return experienceRepository.findById(id);
    }

    public String updateUserExperience(int id, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(id).orElseThrow();
        experience.setCompanyName(request.getCompanyName());
        experience.setRole(request.getRole());
        experience.setStartDate(LocalDate.parse(request.getStartDate()));
        experience.setEndDate(LocalDate.parse(request.getEndDate()));
        experienceRepository.save(experience);
        return "experience edited successfully";
    }
}
