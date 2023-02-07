package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.InventoryProduct;

public interface InventoryProductReponsitory extends JpaRepository<InventoryProduct, Long>{
	InventoryProduct findByName(String name);

}
