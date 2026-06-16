package com.hsms.categoryservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsms.categoryservice.model.CategoryRequestDTO;
import com.hsms.categoryservice.model.CategoryResponseDTO;
import com.hsms.categoryservice.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequestDTO category) {

        try {
            CategoryResponseDTO response = categoryService.addCategory(category);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {

        try {
            CategoryResponseDTO response =
                    categoryService.findCategoryById(categoryId);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {

        try {
            List<CategoryResponseDTO> categories =
                    categoryService.findAllCategories();

            return ResponseEntity.ok(categories);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDTO>>
            getActiveCategories() {

        return ResponseEntity.ok(
                categoryService.getActiveCategories());
    }
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO>
    updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequestDTO category) {

        CategoryResponseDTO response =
                categoryService.updateCategory(
                        categoryId,
                        category);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String>
    deleteCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                categoryService.deleteCategory(
                        categoryId));
    }
}