package com.example.demo.dto;

import java.util.List;

public class ColorDTO {
	private String color;
	
	private String image;
	
	private List<RelatedSizeDTO> relatedSizes;
<<<<<<< HEAD
	
	public ColorDTO() {
		// TODO Auto-generated constructor stub
	}
=======
>>>>>>> 35b31b0f3530a1aca3db6c66a20490c83f77ed7b

	public ColorDTO(String color, String image, List<RelatedSizeDTO> relatedSizes) {
		this.color = color;
		this.image = image;
		this.relatedSizes = relatedSizes;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<RelatedSizeDTO> getRelatedSizes() {
		return relatedSizes;
	}

	public void setRelatedSizes(List<RelatedSizeDTO> relatedSizes) {
		this.relatedSizes = relatedSizes;
	}
	
	
}
