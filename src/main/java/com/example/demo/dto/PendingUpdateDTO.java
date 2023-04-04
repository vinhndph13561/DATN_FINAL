package com.example.demo.dto;

import java.util.List;

public class PendingUpdateDTO {
	
	private Long billId;

	private List<PendingItemDTO> pendingItemDTOs;
	
	public PendingUpdateDTO() {
		// TODO Auto-generated constructor stub
	}

	public PendingUpdateDTO(Long billId, List<PendingItemDTO> pendingItemDTOs) {
		super();
		this.billId = billId;
		this.pendingItemDTOs = pendingItemDTOs;
	}

	public List<PendingItemDTO> getPendingItemDTOs() {
		return pendingItemDTOs;
	}

	public void setPendingItemDTOs(List<PendingItemDTO> pendingItemDTOs) {
		this.pendingItemDTOs = pendingItemDTOs;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
	
}
