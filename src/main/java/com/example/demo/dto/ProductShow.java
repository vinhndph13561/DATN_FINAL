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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Integer getBoughtQuantity() {
		return boughtQuantity;
	}

	public void setBoughtQuantity(Integer boughtQuantity) {
		this.boughtQuantity = boughtQuantity;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public Integer getDecreasePercent() {
		return decreasePercent;
	}

	public void setDecreasePercent(Integer decreasePercent) {
		this.decreasePercent = decreasePercent;
	}
}
