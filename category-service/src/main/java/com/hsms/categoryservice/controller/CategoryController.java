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
	public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO category) {

		CategoryResponseDTO response = categoryService.addCategory(category);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long categoryId) {

		CategoryResponseDTO response = categoryService.findCategoryById(categoryId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {

		return ResponseEntity.ok(categoryService.findAllCategories());
	}

	@GetMapping("/active")
	public ResponseEntity<List<CategoryResponseDTO>> getActiveCategories() {

		return ResponseEntity.ok(categoryService.getActiveCategories());
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long categoryId,
			@RequestBody CategoryRequestDTO category) {

		CategoryResponseDTO response = categoryService.updateCategory(categoryId, category);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {

		return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
	}
}