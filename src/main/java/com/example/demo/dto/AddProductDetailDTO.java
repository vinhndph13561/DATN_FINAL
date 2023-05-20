package com.example.demo.dto;

import com.example.demo.entities.InventoryProductDetail;

import lombok.Data;

@Data
public class AddProductDetailDTO {

	private InventoryProductDetail inventoryProductDetail;
	
	private Double price;
	
	public AddProductDetailDTO() {
		// TODO Auto-generated constructor stub
	}

	public AddProductDetailDTO(InventoryProductDetail inventoryProductDetail, Double price) {
		super();
		this.inventoryProductDetail = inventoryProductDetail;
		this.price = price;
	}

	public InventoryProductDetail getInventoryProductDetail() {
		return inventoryProductDetail;
	}

	public void setInventoryProductDetail(InventoryProductDetail inventoryProductDetail) {
		this.inventoryProductDetail = inventoryProductDetail;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
