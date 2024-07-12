package com.recruiter.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "JOBS-SERVICE")
public interface JobsClient {
    @PostMapping("/api/v1/jobs/blockJob")
    ResponseEntity<String> blockJob(@RequestParam Integer id);
}
