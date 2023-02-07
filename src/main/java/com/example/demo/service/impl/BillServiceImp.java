package com.example.demo.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItem;
import com.example.demo.entities.Bill;
import com.example.demo.entities.BillDetail;
import com.example.demo.entities.User;
import com.example.demo.repository.BillDetailRepository;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BillService;

@Service
@Transactional
public class BillServiceImp implements BillService {
	@Autowired
	private BillRepository billRepository;

	@Autowired
	private CartServiceImp cartService;

	@Autowired
	private ProductServiceImp productService;

	@Autowired
	private BillDetailRepository billDetailRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WebClient webClient;

	@Override
	public List<Bill> getAllBill() {
		return billRepository.findAll();
	}

	@Override
	public Bill saveBill(User user, String paymentType) {
		User user2 = userRepository.getById(1);
		CartDto cartDto = cartService.listCartItem(user);
		List<CartItem> cartItemList = cartDto.getCartItems();
		// create the bill and save it
		Bill bill = new Bill();
		bill.setCustomer(user);
		bill.setStaff(user2);
		bill.setTotal(cartDto.getTotalCost());
		bill.setCreateDay(new Date());
		bill.setPaymentType(paymentType);
		bill.setNote("not thing");
		bill.setStatus(1);
		billRepository.save(bill);
		System.out.println(bill);
		for (CartItem cartItem : cartItemList) {
			// create billdetail and save each one
			BillDetail billDetail = new BillDetail();
			billDetail.setProduct(cartItem.getProduct());
			if (cartItem.getProduct().getSize().equals("XXS")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice());
			}
			if (cartItem.getProduct().getSize().equals("XS")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice() + 5000);
			}
			if (cartItem.getProduct().getSize().equals("S")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice() + 10000);
			}
			if (cartItem.getProduct().getSize().equals("M")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice() + 15000);
			}
			if (cartItem.getProduct().getSize().equals("L")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice() + 20000);
			}
			if (cartItem.getProduct().getSize().equals("XL")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice() + 25000);
			}
			if (cartItem.getProduct().getSize().equals("XXL")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice() + 30000);
			}
			if (cartItem.getProduct().getSize().equals("XXXL")) {
				billDetail.setUnitPrice(cartItem.getProduct().getProduct().getPrice() + 35000);
			}
			billDetail.setQuantity(cartItem.getQuantity());
			billDetail.setBill(bill);
			billDetail.setTotal(cartItem.getQuantity() * billDetail.getUnitPrice());
			billDetailRepository.save(billDetail);
		}
		productService.reductionQuantity(user);
		cartService.deleteUserCartItems(user);
		return bill;
	}

	@Override
	public String removeBillById(Long id) {
		billRepository.deleteById(id);
		return "bill remove" + id;
	}

	@Override
	public Bill getBillById(Long id) {
		return billRepository.findById(id).get();
	}

	@Override
	public List<Bill> findBillByUserId(Integer userId) {
		List<Bill> billList = billRepository.findBillByUserId(userId);
		return billList;
	}

	public List<BillDetail> findBillDetailByBillId(Long id) {
		return billDetailRepository.findByBillId(id);
	}

	@Override
	public boolean cancelBill(Long billId, Integer status) {
		Optional<Bill> foundBill = billRepository.findById(billId);
		if (foundBill.isPresent()) {
			foundBill.get().setDeleteDay(new Date());
			foundBill.get().setStatus(status);
			billRepository.save(foundBill.get());
			return true;
		}
		return false;
	}

	public String getDeliveryOrderDetail(String id) {
		try {
			String json = webClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail-by-client-code")
							.queryParam("client_order_code", id).build())
					.accept(MediaType.APPLICATION_JSON).header("token", "f1e25a8c-6ec0-11ed-b62e-2a5743127145")
					.retrieve().bodyToMono(String.class).block();
			JSONObject jsonObject = new JSONObject(json);
			String dataString = "";
			String status = "";
			Iterator<?> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (key.equals("data")) {
					dataString = jsonObject.get(key).toString();
				}
			}
			JSONObject data = new JSONObject(dataString);
			Iterator<?> keys2 = data.keys();
			while (keys2.hasNext()) {
				String key = (String) keys2.next();
				if (key.equals("status")) {
					status = (String) data.get(key);
				}
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
