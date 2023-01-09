package com.example.demo.dto;

public class RelatedColorDTO {
	private String color;
	
<<<<<<< HEAD
	private boolean isRemained = false;
	
	public RelatedColorDTO() {
		// TODO Auto-generated constructor stub
	}
=======
	private boolean isRemained;
>>>>>>> 35b31b0f3530a1aca3db6c66a20490c83f77ed7b

	public RelatedColorDTO(String color, boolean isRemained) {
		this.color = color;
		this.isRemained = isRemained;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isRemained() {
		return isRemained;
	}

	public void setRemained(boolean isRemained) {
		this.isRemained = isRemained;
	}
	
}
