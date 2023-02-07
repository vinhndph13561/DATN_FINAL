package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BillHistoryDTO;
import com.example.demo.entities.Bill;
import com.example.demo.service.impl.BillServiceImp;

@RestController
@RequestMapping(path = "/api")
public class BillRestController {
	@Autowired
	private BillServiceImp billServiceImp;

	@GetMapping("/bill/historyAdmin/all")
	public List<BillHistoryDTO> getHistoryAll(@RequestParam("user_id") @Nullable Integer userId) {
		return billServiceImp.getAllBillHistory(userId);
	}
	
	@GetMapping("/bill/historyAdmin/pending")
	public List<BillHistoryDTO> getpendingHistory(@RequestParam("user_id") @Nullable Integer userId) {
		return billServiceImp.getPendingBillHistory(userId);
	}
	
	@GetMapping("/bill/historyAdmin/delivering")
	public List<BillHistoryDTO> getdeliveringHistory(@RequestParam("user_id") @Nullable Integer userId) {
		return billServiceImp.getDeliveringBillHistory(userId);
	}
	
	@GetMapping("/bill/historyAdmin/success")
	public List<BillHistoryDTO> getsuccessHistory(@RequestParam("user_id") @Nullable Integer userId) {
		return billServiceImp.getSuccessBillHistory(userId);
	}
	
	@GetMapping("/bill/historyAdmin/cancel")
	public List<BillHistoryDTO> getcancelHistory(@RequestParam("user_id") @Nullable Integer userId) {
		return billServiceImp.getCancelBillHistory(userId);
	}
	
	@GetMapping("/bill/historyAdmin/return")
	public List<BillHistoryDTO> getreturnHistory(@RequestParam("user_id") @Nullable Integer userId) {
		return billServiceImp.getReturnBillHistory(userId);
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
