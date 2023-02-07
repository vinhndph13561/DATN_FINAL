package com.example.demo.dto;

import com.example.demo.entities.InventoryProductDetail;

import lombok.Data;

@Data
public class AddProductDetailDTO {

	private InventoryProductDetail inventoryProductDetail;
	
	private Double price;
}
