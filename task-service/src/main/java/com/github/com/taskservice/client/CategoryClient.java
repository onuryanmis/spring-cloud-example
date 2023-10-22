package com.github.com.taskservice.client;

import com.github.com.taskservice.response.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service")
public interface CategoryClient {

    @GetMapping("/api/v1/category/{id}")
    CategoryDTO findById(@PathVariable String id);
}
