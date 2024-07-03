package com.recruiter.auth_service.controller;

import com.razorpay.RazorpayException;
import com.recruiter.auth_service.dto.PaymentDto;
import com.recruiter.auth_service.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final SubscriptionService subscriptionService;

    public PaymentController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam String amount, @RequestParam Integer userId) {
        try {
            String orderId = subscriptionService.createOrder(amount, userId);
            return ResponseEntity.ok(orderId);
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/update-order")
    public ResponseEntity<Boolean> updateOrder(@RequestBody PaymentDto paymentDto) {
        boolean isUpdated = subscriptionService.updateOrder(paymentDto);
        return ResponseEntity.ok(isUpdated);
    }
}
