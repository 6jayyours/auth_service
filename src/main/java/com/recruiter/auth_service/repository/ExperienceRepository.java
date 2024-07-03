package com.recruiter.auth_service.repository;


import com.recruiter.auth_service.model.resume.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {

    List<Experience> findExperienceByUserId(Integer userId);
}
