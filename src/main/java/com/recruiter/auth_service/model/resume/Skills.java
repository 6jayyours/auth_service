package com.recruiter.auth_service.model.resume;

import com.recruiter.auth_service.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "skills")
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int skillId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String skills;
}
