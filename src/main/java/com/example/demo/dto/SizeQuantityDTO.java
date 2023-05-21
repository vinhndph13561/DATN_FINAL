package com.example.demo.dto;

public class SizeQuantityDTO {
	
	private String size;
	
	private Integer quantity;
	
	public SizeQuantityDTO() {
		// TODO Auto-generated constructor stub
	}

	public SizeQuantityDTO(String size, Integer quantity) {
		super();
		this.size = size;
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
