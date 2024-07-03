package com.recruiter.auth_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDto {

    private String paymentId;
    private String orderId;
    private int planId;
    private int promoId;
    private Integer userId;
}
