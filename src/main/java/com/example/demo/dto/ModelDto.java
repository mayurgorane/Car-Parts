package com.example.demo.dto;

public class ModelDto {
	private Long modelId;
	private String modelname;

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getModelname() {
		return modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public ModelDto(Long modelId, String modelname) {
		super();
		this.modelId = modelId;
		this.modelname = modelname;
	}

	public ModelDto() {
	}

}
