package com.github.com.categoryservice.service.impl;

import com.github.com.categoryservice.request.CategoryRequest;
import com.github.com.categoryservice.entity.Category;
import com.github.com.categoryservice.exception.CategoryNotFoundException;
import com.github.com.categoryservice.mapper.CategoryMapper;
import com.github.com.categoryservice.repository.CategoryRepository;
import com.github.com.categoryservice.response.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    private Category category;

    private CategoryResponse inputCategoryResponse;

    private CategoryRequest inputCategoryRequest;

    @BeforeEach
    public void setUp() {
        category = Category.builder()
                .id(1L)
                .name("category")
                .description("category description")
                .build();

        inputCategoryResponse = CategoryResponse.builder()
                .name("category")
                .description("category description")
                .build();

        inputCategoryRequest = CategoryRequest.builder()
                .name("category")
                .description("category description")
                .build();
    }

    @Test
    @DisplayName("[CategoryService] - create() method")
    public void testCreate() {
        when(categoryMapper.toEntity(inputCategoryRequest)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(inputCategoryResponse);

        CategoryResponse savedCategoryResponse = categoryServiceImpl.create(inputCategoryRequest);

        assertThat(savedCategoryResponse).isNotNull();
        assertThat(savedCategoryResponse.getName()).isEqualTo(inputCategoryRequest.getName());
        assertThat(savedCategoryResponse.getDescription()).isEqualTo(inputCategoryRequest.getDescription());

        verify(categoryMapper, times(1)).toEntity(inputCategoryRequest);
        verify(categoryMapper, times(1)).toResponse(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("[CategoryService] - findAll() method")
    public void testFindAll() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(inputCategoryResponse);

        List<CategoryResponse> categories = categoryServiceImpl.findAll();

        assertThat(categories).isNotNull();
        assertThat(categories).contains(inputCategoryResponse);
        assertThat(categories.size()).isEqualTo(1);

        verify(categoryMapper, times(1)).toResponse(category);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("[CategoryService] - findById() method")
    public void testFindById() {
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(category));
        when(categoryMapper.toResponse(category)).thenReturn(inputCategoryResponse);

        CategoryResponse categoryResponse = categoryServiceImpl.findById(1L);

        assertThat(categoryResponse).isNotNull();
        assertThat(categoryResponse.getName()).isEqualTo(inputCategoryRequest.getName());
        assertThat(categoryResponse.getDescription()).isEqualTo(inputCategoryRequest.getDescription());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toResponse(category);
    }

    @Test
    @DisplayName("[CategoryService] - findById() method when category not found")
    public void testFindByIdWhenCategoryNotFound() {
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryServiceImpl.findById(1L));

        verify(categoryMapper, never()).toResponse(any(Category.class));
    }

    @Test
    @DisplayName("[CategoryServiceImpl] - update() method")
    public void testUpdateCategory() {
        CategoryResponse updatedCategoryResponse = CategoryResponse.builder()
                .name("updated category")
                .description("updated category description")
                .build();

        CategoryRequest updatedCategoryRequest = CategoryRequest.builder()
                .name("updated category")
                .description("updated category description")
                .build();

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(updatedCategoryResponse);

        CategoryResponse result = categoryServiceImpl.update(any(Long.class), updatedCategoryRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updatedCategoryResponse.getName());
        assertThat(result.getDescription()).isEqualTo(updatedCategoryResponse.getDescription());

        verify(categoryRepository, times(1)).findById(any(Long.class));
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toResponse(category);
    }

    @Test
    @DisplayName("[CategoryService] - update() method when category not found")
    public void testUpdateWhenCategoryNotFound() {
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryServiceImpl.update(1L, any(CategoryRequest.class)));

        verify(categoryRepository, times(1)).findById(any(Long.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryMapper, never()).toResponse(any(Category.class));
    }

    @Test
    @DisplayName("[CategoryService] - delete() method")
    public void testDelete() {
        categoryServiceImpl.delete(any(Long.class));

        verify(categoryRepository, times(1)).deleteById(any(Long.class));
    }
}