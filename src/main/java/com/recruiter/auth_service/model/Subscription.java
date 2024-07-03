package com.recruiter.auth_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String orderId;

    private BigDecimal amount;

    private int userId;

    private LocalDateTime payedOn;

    private String paymentId;

    private boolean isPayed;

    public void setStartFrom(LocalDateTime localDateTime) {
    }

    public void setEndOn(LocalDateTime localDateTime) {
    }
}
