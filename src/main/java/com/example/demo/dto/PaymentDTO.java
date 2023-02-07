package com.example.demo.dto;

public class PaymentDTO {
	private String message;
	
	private boolean isPaypal;

	public PaymentDTO(String message, boolean isPaypal) {
		super();
		this.message = message;
		this.isPaypal = isPaypal;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isPaypal() {
		return isPaypal;
	}

	public void setPaypal(boolean isPaypal) {
		this.isPaypal = isPaypal;
	}

	
}
