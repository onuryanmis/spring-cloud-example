package com.github.com.categoryservice.service.impl;

import com.github.com.categoryservice.request.CategoryRequest;
import com.github.com.categoryservice.entity.Category;
import com.github.com.categoryservice.exception.CategoryNotFoundException;
import com.github.com.categoryservice.mapper.CategoryMapper;
import com.github.com.categoryservice.repository.CategoryRepository;
import com.github.com.categoryservice.response.CategoryResponse;
import com.github.com.categoryservice.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryRequest));
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        category = categoryRepository.save(category);

        return categoryMapper.toResponse(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
