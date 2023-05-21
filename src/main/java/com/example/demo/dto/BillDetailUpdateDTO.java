package com.example.demo.dto;

import java.util.List;

public class BillDetailUpdateDTO {

	private Long billDetailId;
	
	private List<ColorOrderDTO> colorOrderDTOs;
	
	public BillDetailUpdateDTO() {
		// TODO Auto-generated constructor stub
	}

	public BillDetailUpdateDTO(Long billDetailId, List<ColorOrderDTO> colorOrderDTOs) {
		super();
		this.billDetailId = billDetailId;
		this.colorOrderDTOs = colorOrderDTOs;
	}

	public Long getBillDetailId() {
		return billDetailId;
	}

	public void setBillDetailId(Long billDetailId) {
		this.billDetailId = billDetailId;
	}

	public List<ColorOrderDTO> getColorOrderDTOs() {
		return colorOrderDTOs;
	}

	public void setColorOrderDTOs(List<ColorOrderDTO> colorOrderDTOs) {
		this.colorOrderDTOs = colorOrderDTOs;
	}
	
	
}
