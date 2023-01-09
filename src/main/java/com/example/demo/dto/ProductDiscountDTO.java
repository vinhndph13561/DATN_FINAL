package com.example.demo.dto;

import com.example.demo.entities.Discount;

public class ProductDiscountDTO {
	private Discount discount;
	
	private boolean isAble;

	public ProductDiscountDTO(Discount discount, boolean isAble) {
		this.discount = discount;
		this.isAble = isAble;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public boolean isAble() {
		return isAble;
	}

	public void setAble(boolean isAble) {
		this.isAble = isAble;
	}
	
	
}
