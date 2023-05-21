package com.example.demo.dto;

import java.util.List;

public class PendingUpdateDTO {
	
	private Long billId;
	
	private Long discountId;

	private List<PendingItemDTO> pendingItemDTOs;
	
	public PendingUpdateDTO() {
		// TODO Auto-generated constructor stub
	}

	

	public PendingUpdateDTO(Long billId, Long discountId, List<PendingItemDTO> pendingItemDTOs) {
		super();
		this.billId = billId;
		this.discountId = discountId;
		this.pendingItemDTOs = pendingItemDTOs;
	}



	public Long getDiscountId() {
		return discountId;
	}



	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
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
