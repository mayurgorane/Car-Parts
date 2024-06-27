package com.example.demo.dto;

public class CarPartCategoryDto {
	private Long categoryId;
	private String categoryName;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public CarPartCategoryDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CarPartCategoryDto(Long categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

}
