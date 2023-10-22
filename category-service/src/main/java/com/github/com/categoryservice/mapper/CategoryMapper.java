package com.github.com.categoryservice.mapper;

import com.github.com.categoryservice.request.CategoryRequest;
import com.github.com.categoryservice.entity.Category;
import com.github.com.categoryservice.response.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest categoryRequest);
}
