package com.example.demo.dto;

import java.util.List;

public class DeletePendingDTO {
	private List<Long> ids;
	
	public DeletePendingDTO() {
		// TODO Auto-generated constructor stub
	}

	public DeletePendingDTO(List<Long> ids) {
		super();
		this.ids = ids;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	
}
