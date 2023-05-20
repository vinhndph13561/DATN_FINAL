package com.example.demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {

	private Integer id;
	private String name;
	private String note;
	private Date createDay;
	private String createdBy;
	private Date modifyDay;
	private String modifiedBy;
	private String status;
	private String image;
	public CategoryDTO() {
		// TODO Auto-generated constructor stub
	}
	public CategoryDTO(Integer id, String name, String note, Date createDay, String createdBy, Date modifyDay,
			String modifiedBy, String status, String image) {
		super();
		this.id = id;
		this.name = name;
		this.note = note;
		this.createDay = createDay;
		this.createdBy = createdBy;
		this.modifyDay = modifyDay;
		this.modifiedBy = modifiedBy;
		this.status = status;
		this.image = image;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getCreateDay() {
		return createDay;
	}
	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifyDay() {
		return modifyDay;
	}
	public void setModifyDay(Date modifyDay) {
		this.modifyDay = modifyDay;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

}
