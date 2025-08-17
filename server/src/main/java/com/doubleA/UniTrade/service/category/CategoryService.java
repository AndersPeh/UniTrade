package com.doubleA.UniTrade.service.category;

import com.doubleA.UniTrade.model.Category;
import com.doubleA.UniTrade.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

// this marks ProductService as a service bean so it will be used as
// concrete instance of ICategoryService in dependency injection.
@Service

// It automatically generates a constructor and store properties marked with final or nonnull
// as fields and parameters (to be assigned value through dependency injection).
// In that constructor, it injects CategoryRepository (it is marked as spring bean)
// and assign it to categoryRepository of CategoryService so CategoryService can interact with the database.
@RequiredArgsConstructor

public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    // This method adds a new category to the database.
    // It checks if the category already exists by its name.
    // If it does not exist, it saves the new category.
    // If it does exist, it throws an EntityExistsException with a message indicating the category
    // that already exists.
    // Use Optional to indicated that the category might not be present in the database.
    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(category1 -> !categoryRepository.existsByName(category1.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() -> new EntityExistsException(category.getName() + "Category already exists"));
    }
    // This method update category information.
    // It first checks if the category with the given ID exists in the database.
    // If it does, it updates the name of the category with the new name provided in the category object.
    // If the category does not exist, it throws an EntityNotFoundException.
    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return Optional.ofNullable(findCategoryById(categoryId)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId).ifPresentOrElse(categoryRepository :: delete, () -> {
            throw new EntityNotFoundException("Category not found.");
        });
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }
}
