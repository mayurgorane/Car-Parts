package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Parts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long partId;

	@NotBlank(message = "Part title cannot be blank")
	@Size(max = 255, message = "Part title cannot be longer than 255 characters")
	private String partTitle;

	private Double partPrice;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private CarPartCategory category;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "model_id")
	private Model model;

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Long getPartId() {
		return partId;
	}

	public void setPartId(Long partId) {
		this.partId = partId;
	}

	public String getPartTitle() {
		return partTitle;
	}

	public void setPartTitle(String partTitle) {
		this.partTitle = partTitle;
	}

	public Double getPartPrice() {
		return partPrice;
	}

	public void setPartPrice(Double partPrice) {
		this.partPrice = partPrice;
	}

	public CarPartCategory getCategory() {
		return category;
	}

	public void setCategory(CarPartCategory category) {
		this.category = category;
	}

}