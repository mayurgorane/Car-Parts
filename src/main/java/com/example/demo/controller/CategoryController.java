package com.example.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.dto.ModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.CarPartCategory;
import com.example.demo.entity.Company;
import com.example.demo.entity.Model;
import com.example.demo.entity.Parts;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.CompanyRepository;
import com.example.demo.repo.ModelRepository;
import com.example.demo.repo.PartsRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
public class CategoryController {
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CompanyRepository carCompanyRepository;

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	PartsRepository partsRepository;

	// Through this method we can create category

	@PostMapping("/category")
	public ResponseEntity<?> createCategory(@Valid @RequestBody CarPartCategory category) {

		Optional<CarPartCategory> existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
		if (existingCategory.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists.");
		}
		CarPartCategory savedCategory = categoryRepository.save(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
	}

	// Through this method we can fetch category

	@GetMapping("/category")
	public ResponseEntity<?> getAllCategories() {
		List<CarPartCategory> categories = categoryRepository.findAll();
		if (categories.isEmpty()) {
			return ResponseEntity.ok("No categories found.");
		}
		return ResponseEntity.ok(categories);
	}

	// Through this method will display all the categories which is associated with
	// car

	@GetMapping("/companies/{companyId}/models/{modelId}/categories")
	public ResponseEntity<?> getCategoriesForCar(@PathVariable Long modelId) {
		if (modelId == null) {
			throw new IllegalArgumentException("Model ID must not be null");
		}

		Optional<Model> modelOptional = modelRepository.findById(modelId);
		if (!modelOptional.isPresent()) {
			throw new EntityNotFoundException("Model not found for ID: " + modelId);
		}

		Model model = modelOptional.get();
		Set<CarPartCategory> categories = new HashSet<>();

		List<Parts> parts = model.getParts();
		for (Parts part : parts) {
			categories.add(part.getCategory());
		}

		if (categories.isEmpty()) {
			return ResponseEntity.ok("No categories found for the car.");
		}

		return ResponseEntity.ok(categories);
	}

	@GetMapping("/companies/{companyId}/categories")
	public ResponseEntity<?> getCategoriesByCompany(@PathVariable Long companyId) {
		Optional<Company> companyOptional = carCompanyRepository.findById(companyId);
		if (companyOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Set<CarPartCategory> categories = new HashSet<>();

		List<Model> models = modelRepository.findAllByCompany_Id(companyId);
		for (Model model : models) {
			List<Parts> parts = model.getParts();
			for (Parts part : parts) {
				categories.add(part.getCategory());
			}
		}

		if (categories.isEmpty()) {
			return ResponseEntity.ok(Collections.singletonMap("message", "No categories found for the company."));
		}

		return ResponseEntity.ok(categories);
	}

	@GetMapping("/models/{modelId}/categories")
	public ResponseEntity<?> getCategoriesByModel(@PathVariable Long modelId) {
		Optional<Model> modelOptional = modelRepository.findById(modelId);
		if (modelOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Model model = modelOptional.get();
		Set<CarPartCategory> categories = new HashSet<>();

		List<Parts> parts = model.getParts();
		for (Parts part : parts) {
			categories.add(part.getCategory());
		}

		if (categories.isEmpty()) {
			return ResponseEntity.ok(Collections.singletonMap("message", "No categories found for the model."));
		}

		Map<Long, Set<CarPartCategory>> modelCategoryMap = new HashMap<>();
		modelCategoryMap.put(modelId, categories);

		return ResponseEntity.ok(modelCategoryMap);
	}
  @GetMapping("/categories/{categoryId}/models")
  public ResponseEntity<?> getModelsByCategoryId(@PathVariable Long categoryId) {
    Optional<CarPartCategory> categoryOptional = categoryRepository.findById(categoryId);
    if (categoryOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
    }

    CarPartCategory category = categoryOptional.get();
    Set<Model> models = new HashSet<>();

    List<Parts> parts = partsRepository.findAllByCategory(category);
    for (Parts part : parts) {
      models.add(part.getModel());
    }

    if (models.isEmpty()) {
      return ResponseEntity.ok("No models found for the category.");
    }

    List<ModelDto> modelDtos = models.stream()
      .map(model -> new ModelDto(model.getModelId(), model.getModelName()))
      .collect(Collectors.toList());

    return ResponseEntity.ok(modelDtos);
  }


}
