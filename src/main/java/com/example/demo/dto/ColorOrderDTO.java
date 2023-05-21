package com.example.demo.dto;

import java.util.List;

public class ColorOrderDTO {

	private String color;
	
	private String image;
	
	private List<SizeQuantityDTO> sizeQuantityDTOs;
	
	public ColorOrderDTO() {
		// TODO Auto-generated constructor stub
	}

	public ColorOrderDTO(String color, String image, List<SizeQuantityDTO> sizeQuantityDTOs) {
		this.color = color;
		this.image = image;
		this.sizeQuantityDTOs = sizeQuantityDTOs;
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

	public List<SizeQuantityDTO> getSizeQuantityDTOs() {
		return sizeQuantityDTOs;
	}

	public void setSizeQuantityDTOs(List<SizeQuantityDTO> sizeQuantityDTOs) {
		this.sizeQuantityDTOs = sizeQuantityDTOs;
	}
	
}
