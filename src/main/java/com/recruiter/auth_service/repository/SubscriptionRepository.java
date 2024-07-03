package com.recruiter.auth_service.repository;

import com.recruiter.auth_service.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Subscription findByOrderId(String orderId);
}
