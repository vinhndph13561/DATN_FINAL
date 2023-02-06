package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Bill;
import com.example.demo.service.impl.BillServiceImp;

@RestController
@RequestMapping(path = "/api")
public class BillRestController {
	@Autowired
	private BillServiceImp billServiceImp;

	@GetMapping("/bill/historyAdmin")
	public List<Bill> historyOrdeAdmin() {
		return billServiceImp.getAllBill();
	}

	@GetMapping("/order/{id}")
	public Bill getOrderById(@PathVariable("id") Long id) {
		return billServiceImp.getBillById(id);
	}

	@GetMapping("")
	List<Bill> getAllApartments() {
		return billServiceImp.getAllBill();
	}

	@GetMapping("/{id}")
	Bill findById(@PathVariable Long id) {
		return billServiceImp.getBillById(id);
	}
	
	@GetMapping("/bill/status")
	public String getHistory() {
		return billServiceImp.getDeliveryOrderDetail("Vinhvip");
	}

}
