package com.github.com.categoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.categoryservice.request.CategoryRequest;
import com.github.com.categoryservice.response.CategoryResponse;
import com.github.com.categoryservice.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("[CategoryController] - index() method")
    public void testIndex() throws Exception {
        CategoryResponse category1 = new CategoryResponse(1L, "Category1", "Category1 description");
        CategoryResponse category2 = new CategoryResponse(2L, "Category2", "Category2 description");
        List<CategoryResponse> categories = Arrays.asList(category1, category2);

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(categories)));

        verify(categoryService, times(1)).findAll();
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("[CategoryController] - show() method")
    public void testShow() throws Exception {
        Long id = 1L;
        CategoryResponse category = new CategoryResponse(id, "Category1", "Category1 description");

        when(categoryService.findById(id)).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(category)));

        verify(categoryService, times(1)).findById(id);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("[CategoryController] - store() method")
    public void testStore() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest("NewCategory", "NewCategory description", true);
        CategoryResponse createdCategory = new CategoryResponse(1L, "NewCategory", "NewCategory description");

        when(categoryService.create(any(CategoryRequest.class))).thenReturn(createdCategory);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(createdCategory)));

        verify(categoryService, times(1)).create(any(CategoryRequest.class));
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("[CategoryController] - update() method")
    public void testUpdate() throws Exception {
        Long id = 1L;
        CategoryRequest categoryRequest = new CategoryRequest("UpdatedCategory", "UpdatedCategory description", true);
        CategoryResponse updatedCategory = new CategoryResponse(id, "UpdatedCategory", "UpdatedCategory description");

        when(categoryService.update(eq(id), any(CategoryRequest.class))).thenReturn(updatedCategory);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(updatedCategory)));

        verify(categoryService, times(1)).update(eq(id), any(CategoryRequest.class));
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("[CategoryController] - destroy() method")
    public void testDestroy() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(categoryService, times(1)).delete(id);
        verifyNoMoreInteractions(categoryService);
    }
}
