package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuantityProduct {
	private String ProductName;
	private String QUantity;
	
	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getQUantity() {
		return QUantity;
	}

	public void setQUantity(String qUantity) {
		QUantity = qUantity;
	}
	
}
