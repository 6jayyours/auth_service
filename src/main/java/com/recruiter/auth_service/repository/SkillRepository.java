package com.recruiter.auth_service.repository;


import com.recruiter.auth_service.model.resume.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Integer> {
    List<Skills> findSkillByUserId(Integer userId);
}
