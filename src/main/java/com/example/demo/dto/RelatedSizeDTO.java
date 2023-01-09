package com.example.demo.dto;

public class RelatedSizeDTO {
	private String size;
	
<<<<<<< HEAD
	private boolean isRemained =false;
	
	public RelatedSizeDTO() {
		// TODO Auto-generated constructor stub
	}
=======
	private boolean isRemained;
>>>>>>> 35b31b0f3530a1aca3db6c66a20490c83f77ed7b

	public RelatedSizeDTO(String size, boolean isRemained) {
		this.size = size;
		this.isRemained = isRemained;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public boolean isRemained() {
		return isRemained;
	}

	public void setRemained(boolean isRemained) {
		this.isRemained = isRemained;
	}
	
	
}
