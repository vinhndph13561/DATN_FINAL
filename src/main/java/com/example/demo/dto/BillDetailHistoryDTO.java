package com.example.demo.dto;

import com.example.demo.entities.ProductDetail;

public class BillDetailHistoryDTO {
	
	private Long id;

	private ProductDetail product;

	private int quantity;

	private double unitPrice;

	private double total;
	
	private String note;
	
	private String status;
	
	public BillDetailHistoryDTO() {
		// TODO Auto-generated constructor stub
	}

	public BillDetailHistoryDTO(Long id, ProductDetail product, int quantity, double unitPrice, double total,
			String note, String status) {
		super();
		this.id = id;
		this.product = product;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.total = total;
		this.note = note;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductDetail getProduct() {
		return product;
	}

	public void setProduct(ProductDetail product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
