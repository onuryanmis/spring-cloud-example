package com.github.com.taskservice.client;

import com.github.com.taskservice.response.CategoryDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service")
public interface CategoryClient {

    @CircuitBreaker(name = "category-service", fallbackMethod = "fallback")
    @GetMapping("/api/v1/category/{id}")
    CategoryDTO findById(@PathVariable String id);

    default CategoryDTO fallback(Exception e) {
        return null;
    }
}
