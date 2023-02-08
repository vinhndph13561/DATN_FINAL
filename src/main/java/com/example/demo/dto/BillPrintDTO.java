package com.example.demo.dto;

import java.util.List;

import com.example.demo.entities.Bill;
import com.example.demo.entities.BillDetail;

public class BillPrintDTO {
	
	private Bill bill;
	private List<BillDetail> details;
	
	private boolean status;
	
	private String message;
	
	public BillPrintDTO() {
		// TODO Auto-generated constructor stub
	}

	public BillPrintDTO(Bill bill, List<BillDetail> details, boolean status, String message) {
		this.bill = bill;
		this.details = details;
		this.status = status;
		this.message = message;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public List<BillDetail> getDetails() {
		return details;
	}

	public void setDetails(List<BillDetail> details) {
		this.details = details;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
