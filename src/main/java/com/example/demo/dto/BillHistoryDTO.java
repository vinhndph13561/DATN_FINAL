package com.example.demo.dto;

import java.util.Date;
import java.util.List;

public class BillHistoryDTO {

	private Long id;

	private Integer customerId;

	private Integer staffId;

	private Date createDay;

	private double total;

	private String paymentType;

	private String note;
	
    private Date deleteDay;

	private List<BillDetailHistoryDTO> billDetails;
	
	private String status;
	
	public BillHistoryDTO() {
		// TODO Auto-generated constructor stub
	}

	public BillHistoryDTO(Long id, Integer customer, Integer staff, Date createDay, double total, String paymentType,
			String status, String note, Date deleteDay, List<BillDetailHistoryDTO> billDetails) {
		this.id = id;
		this.customerId = customer;
		this.staffId = staff;
		this.createDay = createDay;
		this.total = total;
		this.paymentType = paymentType;
		this.status = status;
		this.note = note;
		this.deleteDay = deleteDay;
		this.billDetails = billDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCustomer() {
		return customerId;
	}

	public void setCustomer(Integer customer) {
		this.customerId = customer;
	}

	public Integer getStaff() {
		return staffId;
	}

	public void setStaff(Integer staff) {
		this.staffId = staff;
	}

	public Date getCreateDay() {
		return createDay;
	}

	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDeleteDay() {
		return deleteDay;
	}

	public void setDeleteDay(Date deleteDay) {
		this.deleteDay = deleteDay;
	}

	public List<BillDetailHistoryDTO> getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(List<BillDetailHistoryDTO> billDetails) {
		this.billDetails = billDetails;
	}
	
}
