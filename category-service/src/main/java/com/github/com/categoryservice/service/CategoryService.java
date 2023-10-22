package com.github.com.categoryservice.service;

import com.github.com.categoryservice.request.CategoryRequest;
import com.github.com.categoryservice.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest categoryRequest);

    List<CategoryResponse> findAll();

    CategoryResponse findById(Long id);

    CategoryResponse update(Long id, CategoryRequest categoryRequest);

    void delete(Long id);
}
