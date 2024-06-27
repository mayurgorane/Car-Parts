package com.example.demo.dto;

import java.util.List;

public class FilterDataDto {
	 private List<CompanyDto> companies;
	    private List<ModelDto> models;
	    private List<CarPartCategoryDto> categories;
		public List<CompanyDto> getCompanies() {
			return companies;
		}
		public void setCompanies(List<CompanyDto> companies) {
			this.companies = companies;
		}
		public List<ModelDto> getModels() {
			return models;
		}
		public void setModels(List<ModelDto> models) {
			this.models = models;
		}
		public List<CarPartCategoryDto> getCategories() {
			return categories;
		}
		public void setCategories(List<CarPartCategoryDto> categories) {
			this.categories = categories;
		}
		public FilterDataDto(List<CompanyDto> companies, List<ModelDto> models, List<CarPartCategoryDto> categories) {
			super();
			this.companies = companies;
			this.models = models;
			this.categories = categories;
		}
		public FilterDataDto() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
}
