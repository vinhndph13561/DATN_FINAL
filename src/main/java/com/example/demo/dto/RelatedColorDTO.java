package com.example.demo.dto;

public class RelatedColorDTO {
	private String color;
	
	private String image;
	
	private boolean isRemained = false;
	
	public RelatedColorDTO() {
		// TODO Auto-generated constructor stub
	}

	public RelatedColorDTO(String color, String image, boolean isRemained) {
		this.color = color;
		this.image = image;
		this.isRemained = isRemained;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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
