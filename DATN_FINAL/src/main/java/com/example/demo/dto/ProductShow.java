package com.example.demo.dto;

import com.example.demo.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductShow {
	
	private Product product;
	
	private String rating;
	
	private Integer boughtQuantity;
	
	private boolean isLike;
	
	private Integer decreasePercent;
}
