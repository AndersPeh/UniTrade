package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.model.Category;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.category.ICategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Basically, this controller handles all the requests related to category.
// It maps the incoming HTTP requests to the appropriate service methods and returns the responses.
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
  private final ICategoryService categoryService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllCategories() {
    List<Category> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(new ApiResponse("Success", categories));
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
    Category theCategory = categoryService.addCategory(category);
    return ResponseEntity.ok(new ApiResponse("Success", theCategory));
  }

  @GetMapping("/category/{id}/category")
  public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
    Category theCategory = categoryService.findCategoryById(id);
    return ResponseEntity.ok(new ApiResponse("Success", theCategory));
  }

  @GetMapping("/category/{name}/category")
  public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
    Category theCategory = categoryService.findCategoryByName(name);
    return ResponseEntity.ok(new ApiResponse("Success", theCategory));
  }

  @PutMapping("/category/{id}/update")
  public ResponseEntity<ApiResponse> updateCategory(
      @PathVariable Long id, @RequestBody Category category) {
    Category updatedCategory = categoryService.updateCategory(category, id);
    return ResponseEntity.ok(new ApiResponse("Success", updatedCategory));
  }

  @DeleteMapping("/category/{id}/delete")
  public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok(new ApiResponse("Success", null));
  }
}
