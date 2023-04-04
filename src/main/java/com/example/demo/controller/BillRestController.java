package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BillHistoryDTO;
import com.example.demo.dto.BillUpdateDiscountDTO;
import com.example.demo.dto.PaymentDTO;
import com.example.demo.dto.PendingItemDTO;
import com.example.demo.dto.PendingUpdateDTO;
import com.example.demo.entities.Bill;
import com.example.demo.entities.BillDetail;
import com.example.demo.entities.Interaction;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductDetail;
import com.example.demo.entities.User;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.InteractionRepository;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
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
	private ProductRepository productRepository;
	
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

	@DeleteMapping("bill/pending/delete")
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
	
	@DeleteMapping("bill/pending/deletedetail")
	public PaymentDTO deleteDeliveringBill2(@RequestParam("bill_detail_ids") ArrayList<Long> billDetailIds){
		for (Long long1 : billDetailIds) {
			BillDetail billDetail = billDetailServiceImp.getBillDetailById(long1);
			if (billDetail.getBill().getStatus() == 0) {
				billDetail.setStatus("cancel");
				billDetailServiceImp.saveBillDetail(billDetail);
			}else {
				return new PaymentDTO("Đơn hàng đã được gửi đi vui lòng kiểm tra lại", false);
			}
		}
		return new PaymentDTO("Thành công", true);
	}
	
	@GetMapping("bill/pending/discount")
	public BillUpdateDiscountDTO getDiscountPending(@RequestParam("product_ids") ArrayList<Long> productIds,
													@RequestParam(name = "user") @Nullable Integer userId,
													@RequestParam(name = "total") @Nullable Double total) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}else {
			return null;
		}
		return billServiceImp.listBillShow(user, total, productIds);
	}
	
	@PostMapping("bill/pending/update")
	@Transactional
	public PaymentDTO updateDeliveringBill(@RequestBody PendingUpdateDTO pendingUpdateDTO){
		if (billRepository.getById(pendingUpdateDTO.getBillId()).getStatus() != 0) {
			return new PaymentDTO("Đơn hàng đã được gửi đi vui lòng kiểm tra lại!", false);
		}
		for (PendingItemDTO pendingItemDTO : pendingUpdateDTO.getPendingItemDTOs()) {
			BillDetail billDetail = billDetailServiceImp.getBillDetailById(pendingItemDTO.getId());
			ProductDetail productDetail = productDetailRepository.findBySizeAndColorAndProduct(pendingItemDTO.getSize(),pendingItemDTO.getColor(),billDetail.getProduct().getProduct());
			if(productDetail.getQuantity()==0) {
				return new PaymentDTO("Mẫu bạn đã chọn của sản phẩm "+productDetail.getProduct().getName()+" đã hết, vui lòng chọn mẫu khác!", false);
			}
			if (productDetail.getQuantity() > pendingItemDTO.getQuantity()) {
				billDetail.setQuantity(pendingItemDTO.getQuantity());
				billDetail.setProduct(productDetail);
				billDetailServiceImp.saveBillDetail(billDetail);
			}else {
				return new PaymentDTO("Số lượng mẫu của sản phẩm "+productDetail.getProduct().getName()+" không được vượt quá "+productDetail.getQuantity(), false);
			}
		}
		return new PaymentDTO("Thành công!", true);
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
