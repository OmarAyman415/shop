package com.omar.shop.service.category;

import com.omar.shop.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    String deleteCategory(Long id);

}
