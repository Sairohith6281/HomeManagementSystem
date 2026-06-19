package com.hsms.categoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsms.categoryservice.entity.ServiceCategory;
import com.hsms.categoryservice.exception.DuplicateCategoryException;
import com.hsms.categoryservice.exception.ResourceNotFoundException;
import com.hsms.categoryservice.model.CategoryRequestDTO;
import com.hsms.categoryservice.model.CategoryResponseDTO;
import com.hsms.categoryservice.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

	@Override
	public CategoryResponseDTO addCategory(CategoryRequestDTO category) {
		 if(categoryRepo.existsByCategoryName(
		            category.getCategoryName())) {

		        throw new DuplicateCategoryException(
		                "Category already exists with name : "
		                        + category.getCategoryName());
		    }
		ServiceCategory serviceCategory = new ServiceCategory();

	    serviceCategory.setCategoryName(category.getCategoryName());
	    serviceCategory.setDescription(category.getDescription());
	    serviceCategory.setBasePrice(category.getBasePrice());
	    serviceCategory.setActive(true);
	    
	    ServiceCategory savedCategory = categoryRepo.save(serviceCategory);
	    
	    CategoryResponseDTO response = new CategoryResponseDTO();
	    response.setCategoryId(savedCategory.getCategoryId());
	    response.setCategoryName(savedCategory.getCategoryName());
	    response.setDescription(savedCategory.getDescription());
	    response.setBasePrice(savedCategory.getBasePrice());

	    return response;
	}

	@Override
	public CategoryResponseDTO findCategoryById(Long categoryId) {
		ServiceCategory category = categoryRepo.findById(categoryId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Category not found with id: " + categoryId));

	    CategoryResponseDTO response = new CategoryResponseDTO();
	    response.setCategoryId(category.getCategoryId());
	    response.setCategoryName(category.getCategoryName());
	    response.setDescription(category.getDescription());
	    response.setBasePrice(category.getBasePrice());
	    

	    return response;
	}

	@Override
	public List<CategoryResponseDTO> findAllCategories() {
		 List<ServiceCategory> categories = categoryRepo.findAll();

		    return categories.stream()
		            .map(category -> {
		                CategoryResponseDTO response = new CategoryResponseDTO();
		                response.setCategoryId(category.getCategoryId());
		                response.setCategoryName(category.getCategoryName());
		                response.setDescription(category.getDescription());
		                response.setBasePrice(category.getBasePrice());
		                return response;
		            })
		            .toList();
	}

	@Override
	public List<CategoryResponseDTO> getActiveCategories() {
		List<ServiceCategory> categories =
                categoryRepo.findByActiveTrue();

        return categories.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private CategoryResponseDTO convertToDTO(
            ServiceCategory category) {

        CategoryResponseDTO dto =
                new CategoryResponseDTO();

        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setBasePrice(category.getBasePrice());

        return dto;
    }
    @Override
    public CategoryResponseDTO updateCategory(
            Long categoryId,
            CategoryRequestDTO categoryRequest) {

        ServiceCategory category =
                categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : "
                                        + categoryId));

        category.setCategoryName(
                categoryRequest.getCategoryName());

        category.setDescription(
                categoryRequest.getDescription());

        category.setBasePrice(
                categoryRequest.getBasePrice());

        ServiceCategory updatedCategory =
                categoryRepo.save(category);

        CategoryResponseDTO response =
                new CategoryResponseDTO();

        response.setCategoryId(
                updatedCategory.getCategoryId());

        response.setCategoryName(
                updatedCategory.getCategoryName());

        response.setDescription(
                updatedCategory.getDescription());

        response.setBasePrice(
                updatedCategory.getBasePrice());

        return response;
    }
    @Override
    public String deleteCategory(Long categoryId) {

        ServiceCategory category =
                categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : "
                                        + categoryId));

        category.setActive(false);

        categoryRepo.save(category);

        return "Category deleted successfully";
    }
}