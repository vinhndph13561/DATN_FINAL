package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BillHistoryDTO;
import com.example.demo.dto.PaymentDTO;
import com.example.demo.entities.Bill;
import com.example.demo.entities.BillDetail;
import com.example.demo.entities.Interaction;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductDetail;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.InteractionRepository;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.BillDetailServiceImp;
import com.example.demo.service.impl.BillServiceImp;
import com.example.demo.service.impl.ProductServiceImp;

@RestController
@RequestMapping(path = "/api")
public class BillRestController {
	@Autowired
	private BillServiceImp billServiceImp;
	
	@Autowired
	private BillRepository billRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BillDetailServiceImp billDetailServiceImp;
	
	@Autowired
	private ProductDetailRepository productDetailRepository;
	
	@Autowired
	private InteractionRepository interactionRepository;

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

	@DeleteMapping("bill/delivering/delete")
	public PaymentDTO deleteDeliveringBill(@RequestParam("bill_id") Long billId){
		Bill bill = billServiceImp.getBillById(billId);
		if (bill.getStatus() == 0) {
			bill.setStatus(3);
			billRepository.save(bill);
			return new PaymentDTO("Thành công", true);
		}else {
			return new PaymentDTO("Đơn hàng đã được gửi đi vui lòng kiểm tra lại", false);
		}
	}
	
	@PostMapping("bill/delivering/update")
	public PaymentDTO updateDeliveringBill(@RequestParam("billdetail_id") Long billDetailId,@RequestParam("quantity") Integer quantity){
		BillDetail bill = billDetailServiceImp.getBillDetailById(billDetailId);
		if (bill.getBill().getStatus() == 0) {
			ProductDetail productDetail = productDetailRepository.getById(bill.getProduct().getId());
			if (productDetail.getQuantity() > quantity) {
				bill.setQuantity(quantity);
				billDetailServiceImp.saveBillDetail(bill);
				return new PaymentDTO("Thành công", true);
			}else {
				return new PaymentDTO("Số lượng không được vượt quá "+productDetail.getQuantity(), false);
			}
			
		}else {
			return new PaymentDTO("Đơn hàng đã được gửi đi vui lòng kiểm tra lại", false);
		}
	}
	
	@PostMapping("bill/success/return")
	public PaymentDTO returnBill(@RequestParam("billdetail_id") Long billDetailId,@RequestParam("note") String note){
		BillDetail bill = billDetailServiceImp.getBillDetailById(billDetailId);
		if (((new Date().getTime() - bill.getBill().getCreateDay().getTime()) /(24 * 3600 * 1000)) >7) {
			bill.setStatus("Chờ hoàn");
			bill.setNote(note);
			billDetailServiceImp.saveBillDetail(bill);
			return new PaymentDTO("Tạo yêu cầu thành công", true);
		}else {
			return new PaymentDTO("Đã quá 7 ngày không thể thực hiện yêu cầu", false);
		}
	}
	
	@PostMapping("bill/success/rate")
	public PaymentDTO rateBill(@RequestParam("billdetail_id") Long billDetailId,@RequestParam("comment") String comment,
			@RequestParam("rate") Integer rate,@RequestParam("user_id") @Nullable Integer userId){
		BillDetail bill = billDetailServiceImp.getBillDetailById(billDetailId);
		Interaction interaction = interactionRepository.findByUserIdAndProductId(userId, bill.getProduct().getProduct().getId());
		if (interaction == null) {
			Interaction interaction2 = new Interaction();
			interaction2.setUser(userRepository.getById(userId));
			interaction2.setComment(comment);
			interaction2.setRating(rate);
			interaction2.setProduct(bill.getProduct().getProduct());
			interaction2.setCreateTime(new Date());
			interactionRepository.save(interaction2);
			return new PaymentDTO("Thành công", true);
		}else {
			interaction.setModifyTime(new Date());
			interaction.setComment(comment);
			interaction.setRating(rate);
			interactionRepository.save(interaction);
			return new PaymentDTO("Thành công", true);
		}
		
	}
}
