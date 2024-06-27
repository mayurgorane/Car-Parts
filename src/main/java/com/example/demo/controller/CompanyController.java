package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Company;

import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.CompanyRepository;
import com.example.demo.repo.ModelRepository;
import com.example.demo.repo.PartsRepository;

import jakarta.validation.Valid;

@RestController
public class CompanyController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CompanyRepository carCompanyRepository;

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	PartsRepository partsRepository;

	// Through this method we can create company

	@PostMapping("/companies")
	public ResponseEntity<String> createCompany(@Valid @RequestBody Company company) {
		try {

			if (carCompanyRepository.existsByName(company.getName())) {
				return ResponseEntity.badRequest()
						.body("Company with the name '" + company.getName() + "' already exists");
			}
			carCompanyRepository.save(company);
			return ResponseEntity.ok("Company Added");
		} catch (Exception e) {

			return ResponseEntity.internalServerError().body("An error occurred while creating company");
		}
	}

	// Through this method we can fetch all company

	@GetMapping("/companies")
	public ResponseEntity<?> getAllCompanies() {
		List<Company> companies = carCompanyRepository.findAll();
		if (companies.isEmpty()) {
			return ResponseEntity.ok("No companies found.");
		}
		return ResponseEntity.ok(companies);
	}

}
