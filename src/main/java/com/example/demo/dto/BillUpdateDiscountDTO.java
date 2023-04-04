package com.example.demo.dto;

import java.util.List;

public class BillUpdateDiscountDTO {

	private List<ProductDiscountDTO> discounts;
	
	public BillUpdateDiscountDTO() {
		// TODO Auto-generated constructor stub
	}

	public BillUpdateDiscountDTO(List<ProductDiscountDTO> discounts) {
		super();
		this.discounts = discounts;
	}

	public List<ProductDiscountDTO> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<ProductDiscountDTO> discounts) {
		this.discounts = discounts;
	}
	
}
