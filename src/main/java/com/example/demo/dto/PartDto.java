package com.example.demo.dto;

public class PartDto {
	private Long partId;
	private String partTitle;
	private Double partPrice;
	private String modelName;
	private String categoryName;
	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public PartDto(Long partId, String partTitle, Double partPrice, String modelName, String categoryName,
			String companyName) {
		super();
		this.partId = partId;
		this.partTitle = partTitle;
		this.partPrice = partPrice;
		this.modelName = modelName;
		this.categoryName = categoryName;
		this.companyName = companyName;
	}

	public PartDto() {
		super();

	}

	public PartDto(String string) {
		// TODO Auto-generated constructor stub
	}

}
