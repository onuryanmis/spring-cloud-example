package com.github.com.taskservice.client;

import com.github.com.taskservice.response.CategoryDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryClientTest {

    @Mock
    private CategoryClient categoryClient;

    @BeforeEach
    public void setup() {
        categoryClient = mock(CategoryClient.class);
    }

    @Test
    @DisplayName("[CategoryClient] - findById() method")
    public void testFindCategoryById() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Test Category");
        categoryDTO.setDescription("Test Description");
        categoryDTO.setActive(true);

        when(categoryClient.findById("1")).thenReturn(categoryDTO);

        CategoryDTO result = categoryClient.findById("1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Category", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("[CategoryClient] - findById() method - Not Found")
    public void testFindCategoryByIdNotFound() {
        Mockito.when(categoryClient.findById("2")).thenThrow(FeignException.NotFound.class);

        assertThrows(FeignException.NotFound.class, () -> {
            categoryClient.findById("2");
        });
    }
}
