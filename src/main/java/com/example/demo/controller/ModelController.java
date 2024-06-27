package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.CompanyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ModelDto;
import com.example.demo.entity.Company;
import com.example.demo.entity.Model;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.CompanyRepository;
import com.example.demo.repo.ModelRepository;
import com.example.demo.repo.PartsRepository;

@RestController
public class ModelController {
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CompanyRepository carCompanyRepository;

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	PartsRepository partsRepository;

	// Through this method we can create cars associated with the company

	@GetMapping("/models")
	public ResponseEntity<?> getAllModels() {
		List<Model> models = modelRepository.findAll();
		if (models.isEmpty()) {
			return ResponseEntity.ok(Collections.singletonMap("message", "No models found."));
		}

		List<ModelDto> modelDtos = models.stream().map(model -> new ModelDto(model.getModelId(), model.getModelName()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(modelDtos);
	}

	@GetMapping("/companies/{companyId}/models")
	public ResponseEntity<?> getCarsByCompany(@PathVariable Long companyId) {
		List<Model> cars = modelRepository.findAllByCompany_Id(companyId);

		if (cars.isEmpty()) {
			return ResponseEntity.ok("No cars present.");
		}

		List<ModelDto> carDtos = new ArrayList<>();
		for (Model car : cars) {
			ModelDto modelDto = new ModelDto();
			modelDto.setModelId(car.getModelId());
			modelDto.setModelname(car.getModelName());
			carDtos.add(modelDto);
		}

		return ResponseEntity.ok(carDtos);
	}

	@PostMapping("/companies/{companyId}/models")
	public ResponseEntity<?> createCar(@PathVariable Long companyId, @RequestBody Model model) {

		if (model == null) {
			return ResponseEntity.badRequest().body("Car details cannot be null.");
		}

		if (model.getModelName() == null || model.getModelName().isEmpty()) {
			return ResponseEntity.badRequest().body("Car name cannot be null or empty.");
		}

		Optional<Company> companyOptional = carCompanyRepository.findById(companyId);
		if (!companyOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		model.setCompany(companyOptional.get());

		Model savedCar = modelRepository.save(model);

		ModelDto modelDTO1 = new ModelDto();
		modelDTO1.setModelId(savedCar.getModelId());
		modelDTO1.setModelname(savedCar.getModelName());

		return ResponseEntity.ok(modelDTO1);
	}
  @GetMapping("/models/{modelId}/company")
  public ResponseEntity<?> getCompanyByModelId(@PathVariable Long modelId) {
    Optional<Model> modelOptional = modelRepository.findById(modelId);
    if (modelOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model not found.");
    }

    Model model = modelOptional.get();
    Company company = model.getCompany();

    if (company == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for the given model.");
    }

    CompanyDto companyDto = new CompanyDto();
    companyDto.setCompanyId(company.getId());
    companyDto.setCompanyName(company.getName());

    return ResponseEntity.ok(companyDto);
  }

}
