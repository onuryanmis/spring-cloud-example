package com.github.com.categoryservice.repository;

import com.github.com.categoryservice.entity.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("[CategoryRepository] - save() method")
    public void testSaveCategory() {
        Category category = Category.builder()
                .name("Test Category")
                .description("category description")
                .build();

        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("[CategoryRepository] - findAll() method")
    public void testFindAllCategories() {
        Category category = Category.builder()
                .name("Test Category")
                .description("category description")
                .build();

        categoryRepository.save(category);

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).isNotNull();
        assertThat(categories.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("[CategoryRepository] - findById() method")
    public void testFindCategoryById() {
        Category category = Category.builder()
                .name("Test Category")
                .description("category description")
                .build();

        category = categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(category.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("[CategoryRepository] - deleteById() method")
    public void testDeleteCategoryById() {
        Category category = Category.builder()
                .name("Test Category")
                .description("category description")
                .build();

        category = categoryRepository.save(category);

        categoryRepository.deleteById(category.getId());

        Optional<Category> deletedCategory = categoryRepository.findById(category.getId());

        assertThat(deletedCategory).isEmpty();
    }
}
