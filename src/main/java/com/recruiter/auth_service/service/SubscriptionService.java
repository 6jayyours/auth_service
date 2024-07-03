package com.recruiter.auth_service.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.recruiter.auth_service.dto.PaymentDto;
import com.recruiter.auth_service.model.Subscription;
import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.repository.SubscriptionRepository;
import com.recruiter.auth_service.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SubscriptionService {

    @Value("${razorpay.key_id}")
    private String key;

    @Value("${razorpay.secret_id}")
    private String secret;

    private final UserRepository userRepository;
    private final SubscriptionRepository repository;

    public SubscriptionService(UserRepository userRepository, SubscriptionRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }

    public String createOrder(String amount, Integer userId) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(key, secret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_receipt_11");

        Order order = razorpayClient.orders.create(orderRequest);
        System.out.println(order);
        String orderId = order.get("id");

        try {
            Subscription payments = new Subscription();
            BigDecimal newAmount = new BigDecimal(amount);
            BigDecimal finalAmount = newAmount.divide(BigDecimal.valueOf(100));
            payments.setAmount(finalAmount);
            payments.setOrderId(orderId);
            payments.setUserId(userId);
            repository.save(payments);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    public boolean updateOrder(PaymentDto dto) {
        try {
            Optional<User> user = userRepository.findById(dto.getUserId());
            User subscriber = user.get();
            subscriber.setSubscription(true);
            userRepository.save(subscriber);
            Subscription subscription = repository.findByOrderId(dto.getOrderId());
            subscription.setPaymentId(dto.getPaymentId());
            subscription.setStartFrom(LocalDateTime.now().withHour(0));
            subscription.setEndOn(LocalDateTime.now().plusMonths(6).withHour(23).withMinute(59));
            repository.save(subscription);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
