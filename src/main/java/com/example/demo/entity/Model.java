package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long modelId;

	private String modelName;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "company_Id")
	private Company company;

	@OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
	private List<Parts> parts = new ArrayList<>();

	public List<Parts> getParts() {
		return parts;
	}

	public void setParts(List<Parts> parts) {
		this.parts = parts;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}