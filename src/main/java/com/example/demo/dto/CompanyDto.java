package com.example.demo.dto;

import com.example.demo.entity.Company;

public class CompanyDto {
	private Long companyId;
	private String companyName;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public CompanyDto(Long companyId, String companyName) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
	}

	public CompanyDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompanyDto(Company company) {
		this.companyId = company.getId();
		this.companyName = company.getName();
	}

}
