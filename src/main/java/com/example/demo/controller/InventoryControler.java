package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entities.InventoryProduct;
import com.example.demo.entities.InventoryProductDetail;
import com.example.demo.repository.InventoryProductDetailRepository;
import com.example.demo.repository.InventoryProductReponsitory;

@Controller
public class InventoryControler {
	@Autowired
	InventoryProductReponsitory inventoryProductReponsitory;
	@Autowired
	InventoryProductDetailRepository inventoryProductDetailRepository;
	@GetMapping("/inventory")
	public String inventory(ModelMap map) {
		List<InventoryProduct> list = inventoryProductReponsitory.findAll();
		map.addAttribute("inventory",list);
		return "admin/product/tables_inventorys";
	}
	@GetMapping("/inventoryDetail/view/{id}")
	public String viewInventorydetail(ModelMap map, @PathVariable(value = "id") Long id) {
		map.addAttribute("inventoryDetail",inventoryProductDetailRepository.findById(id).get());
		return "admin/product/tables_inventoryDetail";
	}
}
