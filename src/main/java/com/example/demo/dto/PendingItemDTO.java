package com.example.demo.dto;

public class PendingItemDTO {
	private Long id;
	
	private String color;
	
	private String size;
	
	private Integer quantity;
	
	public PendingItemDTO() {
		// TODO Auto-generated constructor stub
	}

	public PendingItemDTO(Long id, String color, String size, Integer quantity) {
		super();
		this.id = id;
		this.color = color;
		this.size = size;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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
