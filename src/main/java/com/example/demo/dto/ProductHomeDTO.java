package com.example.demo.dto;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Category;

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
public class ProductHomeDTO {
	private List<Category> categories;
	
	private List<ProductShow> allProducts;
	
	private List<ProductShow> topBuy;
	
	private List<ProductShow> topRating;
	
	private List<ProductShow> topLike;
	
	private List<ProductShow> discountProduct;
	
	private List<ProductShow> userLike;
}
