package com.recruiter.auth_service.service;


import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.model.resume.SkillRequest;
import com.recruiter.auth_service.model.resume.Skills;
import com.recruiter.auth_service.repository.SkillRepository;
import com.recruiter.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillsService(SkillRepository skillRepository, UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public String addUserSkill(SkillRequest request) {
        Integer userId = request.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Skills skills = new Skills();
        skills.setUser(user);
        skills.setSkills(request.getSkills());
        skillRepository.save(skills);
        return "success";
    }


    public List<Skills> getUserSkills(Integer userId) {
        return skillRepository.findSkillByUserId(userId);
    }
}
