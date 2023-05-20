package com.example.demo.dto;

import java.io.Serializable;

import com.example.demo.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel implements Serializable{
	private Integer id;
	private String name;
	private float price;
	private String note;
	private Category category;
	public ProductModel(Integer id, String name, float price, String note, Category category) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.note = note;
		this.category = category;
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
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public ProductModel() {
		// TODO Auto-generated constructor stub
	}
}
