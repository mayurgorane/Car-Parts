package com.example.demo.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.CarPartCategoryDto;
import com.example.demo.dto.CompanyDto;
import com.example.demo.dto.ModelDto;
import com.example.demo.dto.PartDto;
import com.example.demo.entity.CarPartCategory;
import com.example.demo.entity.Company;
import com.example.demo.entity.Model;
import com.example.demo.entity.Parts;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.CompanyRepository;
import com.example.demo.repo.ModelRepository;
import com.example.demo.repo.PartsRepository;
import jakarta.validation.Valid;

@RestController
public class PartController {
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CompanyRepository carCompanyRepository;

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	PartsRepository partsRepository;

	@PostMapping("companies/{companyId}/models/{modelId}/categories/{categoryId}/parts")
	public ResponseEntity<?> addPartForCategory(@Valid @PathVariable Long companyId, @PathVariable Long modelId,
			@PathVariable Long categoryId, @RequestBody PartDto partDTO) {

		if (partDTO == null) {
			return ResponseEntity.badRequest().body("Part details cannot be null.");
		}

		if (partDTO.getPartTitle() == null || partDTO.getPartTitle().isEmpty()) {
			return ResponseEntity.badRequest().body("Part title cannot be null or empty.");
		}
		if (partDTO.getPartPrice() == null) {
			return ResponseEntity.badRequest().body("Part price cannot be null.");
		}

		Optional<Company> companyOptional = carCompanyRepository.findById(companyId);
		if (companyOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Optional<Model> modelOptional = modelRepository.findById(modelId);
		if (modelOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Optional<CarPartCategory> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Parts newPart = new Parts();
		newPart.setPartTitle(partDTO.getPartTitle());
		newPart.setPartPrice(partDTO.getPartPrice());

		Model model = modelOptional.get();
		CarPartCategory category = categoryOptional.get();

		newPart.setModel(model);
		newPart.setCategory(category);

		partsRepository.save(newPart);

		return ResponseEntity.status(HttpStatus.CREATED).body("Part successfully added.");
	}

	@GetMapping("/filterParts")
	public ResponseEntity<?> searchParts(@RequestParam(required = false) Long companyId,
			@RequestParam(required = false) Long modelId, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) String partTitle, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "30") int size) {

		Optional<Model> modelOptional = Optional.empty();
		Optional<CarPartCategory> categoryOptional = Optional.empty();

		if (modelId != null) {
			modelOptional = modelRepository.findById(modelId);
		}

		if (categoryId != null) {
			categoryOptional = categoryRepository.findById(categoryId);
			if (categoryOptional.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parts not found");
			}
		}

		List<Parts> parts = new ArrayList<>();

		if (companyId != null && modelOptional.isEmpty()) {
			List<Model> modelsForCompany = modelRepository.findByCompanyId(companyId);

			for (Model model : modelsForCompany) {
				if (categoryOptional.isPresent()) {
					if (partTitle != null) {
						parts.addAll(partsRepository.findByCategoryAndModelAndPartTitle(categoryOptional.get(), model,
								partTitle, PageRequest.of(page, size)).getContent());
					} else {
						parts.addAll(partsRepository
								.findByModelAndCategory(model, categoryOptional.get(), PageRequest.of(page, size))
								.getContent());
					}
				} else {
					if (partTitle != null) {
						parts.addAll(partsRepository
								.findByModelAndPartTitle(model, partTitle, PageRequest.of(page, size)).getContent());
					} else {
						parts.addAll(partsRepository.findByModel(model, PageRequest.of(page, size)).getContent());
					}
				}
			}
		} else if (modelOptional.isPresent()) {
			if (categoryOptional.isPresent()) {
				if (partTitle != null) {
					parts = partsRepository.findByCategoryAndModelAndPartTitle(categoryOptional.get(),
							modelOptional.get(), partTitle, PageRequest.of(page, size)).getContent();
				} else {
					parts = partsRepository.findByModelAndCategory(modelOptional.get(), categoryOptional.get(),
							PageRequest.of(page, size)).getContent();
				}
			} else {
				if (partTitle != null) {
					parts = partsRepository
							.findByModelAndPartTitle(modelOptional.get(), partTitle, PageRequest.of(page, size))
							.getContent();
				} else {
					parts = partsRepository.findByModel(modelOptional.get(), PageRequest.of(page, size)).getContent();
				}
			}
		} else if (categoryId != null) {
			if (partTitle != null) {
				parts = partsRepository
						.findByCategoryAndPartTitle(categoryOptional.get(), partTitle, PageRequest.of(page, size))
						.getContent();
			} else {
				parts = partsRepository.findByCategory(categoryOptional.get(), PageRequest.of(page, size)).getContent();
			}
		} else if (partTitle != null) {
			parts = partsRepository.findByPartTitle(partTitle, PageRequest.of(page, size)).getContent();
		} else {
			parts = partsRepository.findAll(PageRequest.of(page, size)).getContent();
		}

		if (parts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parts not found");
		}

		List<PartDto> partDtos = parts.stream().map(part -> {
			PartDto partDto = new PartDto();
			partDto.setPartId(part.getPartId());
			partDto.setPartTitle(part.getPartTitle());
			partDto.setPartPrice(part.getPartPrice());
			partDto.setCompanyName(part.getModel().getCompany().getName());
			partDto.setModelName(part.getModel().getModelName());
			partDto.setCategoryName(part.getCategory().getCategoryName());
			return partDto;
		}).collect(Collectors.toList());

		return ResponseEntity.ok().body(partDtos);
	}

	@PutMapping("/filterParts/{partId}")
	public ResponseEntity<?> updatePart(@PathVariable Long partId, @Valid @RequestBody Parts updatedPart) {

		if (updatedPart == null) {
			return ResponseEntity.badRequest().body("Part details cannot be null.");
		}

		if (updatedPart.getPartTitle() == null || updatedPart.getPartTitle().isEmpty()) {
			return ResponseEntity.badRequest().body("Part title cannot be null or empty.");
		}

		if (updatedPart.getPartPrice() == null) {
			return ResponseEntity.badRequest().body("Part price cannot be null.");
		}

		Optional<Parts> partOptional = partsRepository.findById(partId);
		if (partOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Parts existingPart = partOptional.get();
		existingPart.setPartTitle(updatedPart.getPartTitle());
		existingPart.setPartPrice(updatedPart.getPartPrice());

		partsRepository.save(existingPart);
		return ResponseEntity.ok("Part successfully updated.");
	}

	@DeleteMapping("/filterParts/{partId}")
	public ResponseEntity<?> deletePart(@PathVariable Long partId) {
		Optional<Parts> partOptional = partsRepository.findById(partId);
		if (partOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		partsRepository.deleteById(partId);
		return ResponseEntity.ok("Part with ID " + partId + " has been successfully deleted.");
	}

	@GetMapping("/getList")
	public ResponseEntity<?> getList(@RequestParam(required = false) Long companyId,
			@RequestParam(required = false) Long modelId, @RequestParam(required = false) Long categoryId) {

		if (modelId == null && categoryId == null) {

			if (companyId == null) {

				List<Company> companies = carCompanyRepository.findAll();
				if (companies.isEmpty()) {
					return ResponseEntity.ok(Collections.singletonMap("message", "No companies found."));
				}
				List<CompanyDto> companyDtos = companies.stream()
						.map(company -> new CompanyDto(company.getId(), company.getName()))
						.collect(Collectors.toList());
				return ResponseEntity.ok(companyDtos);
			} else {

				List<Model> models = modelRepository.findAllByCompany_Id(companyId);
				if (models.isEmpty()) {
					return ResponseEntity.ok(Collections.singletonMap("message", "No models found for the company."));
				}
				List<ModelDto> modelDtos = models.stream()
						.map(model -> new ModelDto(model.getModelId(), model.getModelName()))
						.collect(Collectors.toList());
				return ResponseEntity.ok(modelDtos);
			}
		} else if (modelId != null) {

			Optional<Model> modelOptional = modelRepository.findById(modelId);
			if (modelOptional.isPresent()) {
				Model model = modelOptional.get();
				Set<CarPartCategory> categories = new HashSet<>();
				List<Parts> modelParts = model.getParts();
				for (Parts part : modelParts) {
					categories.add(part.getCategory());
				}
				if (categories.isEmpty()) {
					return ResponseEntity.ok(Collections.singletonMap("message", "No categories found for the model."));
				}
				List<CarPartCategoryDto> categoryDtos = categories.stream()
						.map(category -> new CarPartCategoryDto(category.getCategoryId(), category.getCategoryName()))
						.collect(Collectors.toList());
				return ResponseEntity.ok(categoryDtos);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else if (categoryId != null) {

			Optional<CarPartCategory> categoryOptional = categoryRepository.findById(categoryId);
			if (categoryOptional.isPresent()) {
				CarPartCategory category = categoryOptional.get();
				List<Parts> parts = partsRepository.findByCategory(category);
				if (parts.isEmpty()) {
					return ResponseEntity.ok(Collections.singletonMap("message", "No parts found for the category."));
				}
				return ResponseEntity.ok(parts);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {

			return ResponseEntity.ok(Collections.singletonMap("message", "Invalid request."));
		}
	}

}
