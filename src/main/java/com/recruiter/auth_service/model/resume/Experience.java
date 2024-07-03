package com.recruiter.auth_service.model.resume;

import com.recruiter.auth_service.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "experience")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;


}
