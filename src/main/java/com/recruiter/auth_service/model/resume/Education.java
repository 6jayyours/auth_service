package com.recruiter.auth_service.model.resume;

import com.recruiter.auth_service.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "education")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int educationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String collegeName;
    private String year;
    private String degree;
    private String place;

}
