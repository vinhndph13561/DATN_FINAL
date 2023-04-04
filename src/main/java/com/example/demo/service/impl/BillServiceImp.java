package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.BillDetailHistoryDTO;
import com.example.demo.dto.BillHistoryDTO;
import com.example.demo.dto.BillUpdateDiscountDTO;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItem;
import com.example.demo.dto.CartShowDTO;
import com.example.demo.dto.ProductDiscountDTO;
import com.example.demo.entities.Bill;
import com.example.demo.entities.BillDetail;
import com.example.demo.entities.Cart;
import com.example.demo.entities.Discount;
import com.example.demo.entities.DiscountDetail;
import com.example.demo.entities.User;
import com.example.demo.repository.BillDetailRepository;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.DiscountDetailRepository;
import com.example.demo.repository.DiscountRepository;
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
	private DiscountRepository discountRepository;
	
	@Autowired
	private DiscountDetailRepository discountDetailRepository;

	@Autowired
	private WebClient webClient;

	@Override
	public List<Bill> getAllBill() {
		return billRepository.findAll();
	}

	@Override
	public Bill saveBill(User user, String paymentType, Double decrease, User user2) {
		if (user2 == null) {
			user2 = userRepository.getById(1);
		}
		CartDto cartDto = cartService.listCartItem(user);
		List<CartItem> cartItemList = cartDto.getCartItems();
		// create the bill and save it
		boolean state = productService.reductionQuantity(user);
		if (state == false) {
			return null;
		}
		Bill bill = new Bill();
		bill.setCustomer(user);
		bill.setStaff(user2);
		if (decrease == null) {
			bill.setTotal(cartDto.getTotalCost());
		} else {
			bill.setTotal(cartDto.getTotalCost() - decrease);
		}
		bill.setCreateDay(new Date());
		bill.setPaymentType(paymentType);
		bill.setNote("");
		bill.setStatus(0);
		billRepository.save(bill);
		System.out.println(bill);
		for (CartItem cartItem : cartItemList) {
			// create billdetail and save each one
			BillDetail billDetail = new BillDetail();
			billDetail.setProduct(cartItem.getProduct());
			billDetail.setUnitPrice(cartItem.getNewPrice());
			billDetail.setQuantity(cartItem.getQuantity());
			billDetail.setBill(bill);
			billDetail.setStatus("pending");
			billDetail.setTotal(cartItem.getQuantity() * billDetail.getUnitPrice());
			billDetailRepository.save(billDetail);
		}
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
		List<Bill> billList = billRepository.findByCustomerId(userId);
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

	public List<BillHistoryDTO> getAllBillHistory(Integer userId) {
		List<Bill> bills = billRepository.findByCustomerId(userId);
		List<BillHistoryDTO> lBillHistoryDTOs = new ArrayList<>();
		for (Bill bill : bills) {
			System.out.println(bill.getId());
			BillHistoryDTO billHistoryDTO = new BillHistoryDTO();
			billHistoryDTO.setId(bill.getId());
			billHistoryDTO.setCustomer(bill.getCustomer().getId());

			billHistoryDTO.setStaff(bill.getStaff().getId());

			billHistoryDTO.setCreateDay(bill.getCreateDay());

			billHistoryDTO.setTotal(bill.getTotal());

			billHistoryDTO.setPaymentType(bill.getPaymentType());
			billHistoryDTO.setNote(bill.getNote());
			billHistoryDTO.setDeleteDay(bill.getDeleteDay());
			if (bill.getStatus() == 0) {
				billHistoryDTO.setStatus("Chờ duyệt");
			}
			if (bill.getStatus() == 1) {
				billHistoryDTO.setStatus("Đang chuyển");
			}
			if (bill.getStatus() == 2) {
				billHistoryDTO.setStatus("Đã nhận");
			}
			if (bill.getStatus() == 3) {
				billHistoryDTO.setStatus("Đã hủy");
			}
			if (bill.getStatus() == 4) {
				billHistoryDTO.setStatus("Đã hoàn");
			}
			List<BillDetailHistoryDTO> billDetails = new ArrayList<>();
			
			for (BillDetail billDetail : bill.getBillDetails()) {
				System.out.println(billDetail.getId());
				BillDetailHistoryDTO billDetailHistoryDTO = new BillDetailHistoryDTO();
				billDetailHistoryDTO.setId(billDetail.getId());
				billDetailHistoryDTO.setProductDetail(billDetail.getProduct());
				billDetailHistoryDTO.setQuantity(billDetail.getQuantity());
				billDetailHistoryDTO.setUnitPrice(billDetail.getUnitPrice());
				billDetailHistoryDTO.setTotal(billDetail.getTotal());
				billDetailHistoryDTO.setStatus(billDetail.getStatus());
				billDetailHistoryDTO.setNote(billDetail.getNote());
				billDetails.add(billDetailHistoryDTO);
			}
			billHistoryDTO.setBillDetails(billDetails);
			lBillHistoryDTOs.add(billHistoryDTO);
		}
		System.out.println(lBillHistoryDTOs);
		return lBillHistoryDTOs;
	}

	public List<BillHistoryDTO> getCancelBillHistory(Integer userId) {
		List<Bill> bills = billRepository.findByCustomerId(userId);
		List<BillHistoryDTO> lBillHistoryDTOs = new ArrayList<>();
		for (Bill bill : bills) {
			if (bill.getStatus() == 0 || bill.getStatus() == 3) {
				BillHistoryDTO billHistoryDTO = new BillHistoryDTO();
				billHistoryDTO.setId(bill.getId());
				billHistoryDTO.setCustomer(bill.getCustomer().getId());
	
				billHistoryDTO.setStaff(bill.getStaff().getId());
	
				billHistoryDTO.setCreateDay(bill.getCreateDay());
	
				billHistoryDTO.setTotal(bill.getTotal());
	
				billHistoryDTO.setPaymentType(bill.getPaymentType());
				billHistoryDTO.setNote(bill.getNote());
				billHistoryDTO.setDeleteDay(bill.getDeleteDay());
				billHistoryDTO.setStatus("Đã hủy");
				List<BillDetailHistoryDTO> billDetails = new ArrayList<>();
	
				for (BillDetail billDetail : bill.getBillDetails()) {
					if (billDetail.getStatus().equals("cancel")) {
						BillDetailHistoryDTO billDetailHistoryDTO = new BillDetailHistoryDTO();
						billDetailHistoryDTO.setId(billDetail.getId());
						billDetailHistoryDTO.setProductDetail(billDetail.getProduct());
						billDetailHistoryDTO.setQuantity(billDetail.getQuantity());
						billDetailHistoryDTO.setUnitPrice(billDetail.getUnitPrice());
						billDetailHistoryDTO.setTotal(billDetail.getTotal());
						billDetailHistoryDTO.setStatus(billDetail.getStatus());
						billDetailHistoryDTO.setNote(billDetail.getNote());
						billDetails.add(billDetailHistoryDTO);
					}
				}
				billHistoryDTO.setBillDetails(billDetails);
				lBillHistoryDTOs.add(billHistoryDTO);
			}
		}
		return lBillHistoryDTOs;
	}

	public List<BillHistoryDTO> getPendingBillHistory(Integer userId) {
		List<Bill> bills = billRepository.findByCustomerIdAndStatus(userId, 0);
		List<BillHistoryDTO> lBillHistoryDTOs = new ArrayList<>();
		for (Bill bill : bills) {
			BillHistoryDTO billHistoryDTO = new BillHistoryDTO();
			billHistoryDTO.setId(bill.getId());
			billHistoryDTO.setCustomer(bill.getCustomer().getId());

			billHistoryDTO.setStaff(bill.getStaff().getId());

			billHistoryDTO.setCreateDay(bill.getCreateDay());

			billHistoryDTO.setTotal(bill.getTotal());

			billHistoryDTO.setPaymentType(bill.getPaymentType());
			billHistoryDTO.setNote(bill.getNote());
			billHistoryDTO.setDeleteDay(bill.getDeleteDay());
			billHistoryDTO.setStatus("Chờ duyệt");
			List<BillDetailHistoryDTO> billDetails = new ArrayList<>();
			for (BillDetail billDetail : bill.getBillDetails()) {
				BillDetailHistoryDTO billDetailHistoryDTO = new BillDetailHistoryDTO();
				if (billDetail.getStatus().equals("pending")) {
					billDetailHistoryDTO.setId(billDetail.getId());
					billDetailHistoryDTO.setProductDetail(billDetail.getProduct());
					billDetailHistoryDTO.setQuantity(billDetail.getQuantity());
					billDetailHistoryDTO.setUnitPrice(billDetail.getUnitPrice());
					billDetailHistoryDTO.setTotal(billDetail.getTotal());
					billDetailHistoryDTO.setStatus(billDetail.getStatus());
					billDetailHistoryDTO.setNote(billDetail.getNote());
					billDetails.add(billDetailHistoryDTO);
				}
			}
			billHistoryDTO.setBillDetails(billDetails);
			lBillHistoryDTOs.add(billHistoryDTO);
		}
		return lBillHistoryDTOs;
	}
	
	public List<BillHistoryDTO> getDeliveringBillHistory(Integer userId) {
		List<Bill> bills = billRepository.findByCustomerIdAndStatus(userId, 1);
		List<BillHistoryDTO> lBillHistoryDTOs = new ArrayList<>();
		for (Bill bill : bills) {
			BillHistoryDTO billHistoryDTO = new BillHistoryDTO();
			billHistoryDTO.setId(bill.getId());
			billHistoryDTO.setCustomer(bill.getCustomer().getId());

			billHistoryDTO.setStaff(bill.getStaff().getId());

			billHistoryDTO.setCreateDay(bill.getCreateDay());

			billHistoryDTO.setTotal(bill.getTotal());

			billHistoryDTO.setPaymentType(bill.getPaymentType());
			billHistoryDTO.setNote(bill.getNote());
			billHistoryDTO.setDeleteDay(bill.getDeleteDay());
			billHistoryDTO.setStatus("Đang chuyển");
			List<BillDetailHistoryDTO> billDetails = new ArrayList<>();
			for (BillDetail billDetail : bill.getBillDetails()) {
				if (billDetail.getStatus().equals("delivering")) {
					BillDetailHistoryDTO billDetailHistoryDTO = new BillDetailHistoryDTO();
					billDetailHistoryDTO.setId(billDetail.getId());
					billDetailHistoryDTO.setProductDetail(billDetail.getProduct());
					billDetailHistoryDTO.setQuantity(billDetail.getQuantity());
					billDetailHistoryDTO.setUnitPrice(billDetail.getUnitPrice());
					billDetailHistoryDTO.setTotal(billDetail.getTotal());
					billDetailHistoryDTO.setStatus(billDetail.getStatus());
					billDetailHistoryDTO.setNote(billDetail.getNote());
					billDetails.add(billDetailHistoryDTO);
				}
			}
			billHistoryDTO.setBillDetails(billDetails);
			lBillHistoryDTOs.add(billHistoryDTO);
		}
		return lBillHistoryDTOs;
	}

	public List<BillHistoryDTO> getSuccessBillHistory(Integer userId) {
		List<Bill> bills = billRepository.findByCustomerIdAndStatus(userId, 2);
		List<BillHistoryDTO> lBillHistoryDTOs = new ArrayList<>();
		for (Bill bill : bills) {
			BillHistoryDTO billHistoryDTO = new BillHistoryDTO();
			billHistoryDTO.setId(bill.getId());
			billHistoryDTO.setCustomer(bill.getCustomer().getId());
			billHistoryDTO.setStaff(bill.getStaff().getId());
			billHistoryDTO.setCreateDay(bill.getCreateDay());
			billHistoryDTO.setTotal(bill.getTotal());
			billHistoryDTO.setPaymentType(bill.getPaymentType());
			billHistoryDTO.setNote(bill.getNote());
			billHistoryDTO.setDeleteDay(bill.getDeleteDay());
			billHistoryDTO.setStatus("Đã nhận");

			List<BillDetailHistoryDTO> billDetails = new ArrayList<>();
			for (BillDetail billDetail : bill.getBillDetails()) {
				if (billDetail.getStatus().equals("success")) {
					BillDetailHistoryDTO billDetailHistoryDTO = new BillDetailHistoryDTO();
					billDetailHistoryDTO.setId(billDetail.getId());
					billDetailHistoryDTO.setProductDetail(billDetail.getProduct());
					billDetailHistoryDTO.setQuantity(billDetail.getQuantity());
					billDetailHistoryDTO.setUnitPrice(billDetail.getUnitPrice());
					billDetailHistoryDTO.setTotal(billDetail.getTotal());
					billDetailHistoryDTO.setStatus(billDetail.getStatus());
					billDetailHistoryDTO.setNote(billDetail.getNote());
					billDetails.add(billDetailHistoryDTO);
				}
			}
			billHistoryDTO.setBillDetails(billDetails);
			lBillHistoryDTOs.add(billHistoryDTO);
		}
		return lBillHistoryDTOs;
	}

	public List<BillHistoryDTO> getReturnBillHistory(Integer userId) {
		List<Bill> bills = billRepository.findByCustomerId(userId);
		List<BillHistoryDTO> lBillHistoryDTOs = new ArrayList<>();
		for (Bill bill : bills) {
			if (bill.getStatus() == 2 || bill.getStatus() == 4) {
				BillHistoryDTO billHistoryDTO = new BillHistoryDTO();
				billHistoryDTO.setId(bill.getId());
				billHistoryDTO.setCustomer(bill.getCustomer().getId());				
				billHistoryDTO.setStaff(bill.getStaff().getId());
				billHistoryDTO.setCreateDay(bill.getCreateDay());
				billHistoryDTO.setTotal(bill.getTotal());
				billHistoryDTO.setPaymentType(bill.getPaymentType());
				billHistoryDTO.setNote(bill.getNote());
				billHistoryDTO.setDeleteDay(bill.getDeleteDay());
				if (bill.getStatus() == 2) {
					billHistoryDTO.setStatus("Đã nhận");
				}
				if (bill.getStatus() == 4) {
					billHistoryDTO.setStatus("Đã hoàn");
				}
				List<BillDetailHistoryDTO> billDetails = new ArrayList<>();
				for (BillDetail billDetail : bill.getBillDetails()) {
					if (billDetail.getStatus().equals("return")) {
						BillDetailHistoryDTO billDetailHistoryDTO = new BillDetailHistoryDTO();
						billDetailHistoryDTO.setId(billDetail.getId());
						billDetailHistoryDTO.setProductDetail(billDetail.getProduct());
						billDetailHistoryDTO.setQuantity(billDetail.getQuantity());
						billDetailHistoryDTO.setUnitPrice(billDetail.getUnitPrice());
						billDetailHistoryDTO.setTotal(billDetail.getTotal());
						billDetailHistoryDTO.setStatus(billDetail.getStatus());
						billDetailHistoryDTO.setNote(billDetail.getNote());
						billDetails.add(billDetailHistoryDTO);
					}
				}
				billHistoryDTO.setBillDetails(billDetails);
				lBillHistoryDTOs.add(billHistoryDTO);
			}
		}
		return lBillHistoryDTOs;
	}
	
	public BillUpdateDiscountDTO listBillShow(User user,Double price, ArrayList<Long> productId) {
        List<ProductDiscountDTO> productDiscounts = new ArrayList<>();
		for (Discount discount : discountRepository.findByEndDayAfter(new Date())) {
			ProductDiscountDTO productDiscountDTO = new ProductDiscountDTO();
			if (discount.getMemberType() == null) {
				if (discount.getQuantity()==0) {
					productDiscountDTO.setDiscount(discount);
					productDiscountDTO.setAble(false); 
					productDiscountDTO.setReason("Hết mã");
					productDiscounts.add(productDiscountDTO);	
				}
				if (price > discount.getCondition()) {
					productDiscountDTO.setDiscount(discount);
					productDiscountDTO.setAble(true); 
					productDiscounts.add(productDiscountDTO);
				}else {
					productDiscountDTO.setDiscount(discount);
					productDiscountDTO.setAble(false); 
					productDiscountDTO.setReason("Giá trị đơn hàng chưa đủ điều kiện áp dụng mã này");
					productDiscounts.add(productDiscountDTO);	
				}
			}
			else if (discount.getMemberType().equalsIgnoreCase("thuong")) {
				if (discount.getQuantity()==0) {
					productDiscountDTO.setDiscount(discount);
					productDiscountDTO.setAble(false); 
					productDiscountDTO.setReason("Hết mã");
					productDiscounts.add(productDiscountDTO);	
				}
				if (price > discount.getCondition()) {
					productDiscountDTO.setDiscount(discount);
					productDiscountDTO.setAble(true); 
					productDiscounts.add(productDiscountDTO);
				}else {
					productDiscountDTO.setDiscount(discount);
					productDiscountDTO.setAble(false); 
					productDiscountDTO.setReason("Giá trị đơn hàng chưa đủ điều kiện áp dụng mã này");
					productDiscounts.add(productDiscountDTO);	
				}	
			}
			else if (discount.getMemberType().equalsIgnoreCase("truc tiep")) {
				continue;
			}
			else if (discount.getMemberType().equalsIgnoreCase("nganh hang")) {
				Set<Long> lst = new HashSet<>();
				for (Long id : productId) {
					DiscountDetail discountDetail = discountDetailRepository.findByProductAndDiscount(productService.getProductById(id),discount);
					if (discountDetail==null) {
						productDiscountDTO.setDiscount(discount);
						productDiscountDTO.setAble(false); 
						productDiscountDTO.setReason("Mã này không áp dụng với sản phẩm trong giỏ hàng");
						productDiscounts.add(productDiscountDTO);
						lst.clear();
						break;
					}else {
						lst.add(discountDetail.getDiscount().getId());
					}
				}		
				if (lst.size()==1) {
					if (price > discount.getCondition()) {
						productDiscountDTO.setDiscount(discount);
						productDiscountDTO.setAble(true); 
						productDiscounts.add(productDiscountDTO);
					}else {
						productDiscountDTO.setDiscount(discount);
						productDiscountDTO.setAble(false); 
						productDiscountDTO.setReason("Giá trị đơn hàng chưa đủ điều kiện áp dụng mã này");
						productDiscounts.add(productDiscountDTO);	
					}	
				}
				if (lst.size()>1) {
					productDiscountDTO.setDiscount(discount);
					productDiscountDTO.setAble(false); 
					productDiscountDTO.setReason("Mã này chỉ áp dụng cho một số sản phẩm nhất định");
					productDiscounts.add(productDiscountDTO);	
				}
			}
			else if (user == null || user.getIsMember() == 0) {
				productDiscountDTO.setDiscount(discount);
				productDiscountDTO.setAble(false); 
				productDiscountDTO.setReason("Mã chỉ dành cho thành viên");
				productDiscounts.add(productDiscountDTO);
			}
			else {
				if (user.getMemberType().equalsIgnoreCase("đồng")) {
					if (discount.getMemberType().equalsIgnoreCase(user.getMemberType())) {
						if (discount.getQuantity()==0) {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(false); 
							productDiscountDTO.setReason("Hết mã");
							productDiscounts.add(productDiscountDTO);	
						}if (price > discount.getCondition()) {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(true);
							productDiscountDTO.setReason("Mã đồng");
							productDiscounts.add(productDiscountDTO);
						}else {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(false); 
							productDiscountDTO.setReason("Giá trị đơn hàng chưa đủ điều kiện áp dụng mã này");
							productDiscounts.add(productDiscountDTO);	
						}					
					}else {
						productDiscountDTO.setDiscount(discount);
						productDiscountDTO.setAble(false); 
						productDiscountDTO.setReason("Bạn cần nâng cấp thành viên "+discount.getMemberType() +" để sử dụng mã này");
						productDiscounts.add(productDiscountDTO);
					}
				}
				
				if (user.getMemberType().equalsIgnoreCase("bạc")) {
					if (discount.getMemberType().equalsIgnoreCase(user.getMemberType()) || discount.getMemberType().equalsIgnoreCase("đồng")) {
						if (discount.getQuantity()==0) {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(false); 
							productDiscountDTO.setReason("Hết mã");
							productDiscounts.add(productDiscountDTO);	
						}if (price > discount.getCondition()) {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(true);
							productDiscountDTO.setReason("Mã "+discount.getMemberType());
							productDiscounts.add(productDiscountDTO);
						}else {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(false); 
							productDiscountDTO.setReason("Giá trị đơn hàng chưa đủ điều kiện áp dụng mã này");
							productDiscounts.add(productDiscountDTO);	
						}									
					}else {
						productDiscountDTO.setDiscount(discount);
						productDiscountDTO.setAble(false); 
						productDiscountDTO.setReason("Bạn cần nâng cấp thành viên "+discount.getMemberType() +" để sử dụng mã này");
						productDiscounts.add(productDiscountDTO);
					}
				}
				if (user.getMemberType().equalsIgnoreCase("vàng")) {
						if (discount.getQuantity()==0) {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(false); 
							productDiscountDTO.setReason("Hết mã");
							productDiscounts.add(productDiscountDTO);	
						}if (price > discount.getCondition()) {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(true);
							productDiscountDTO.setReason("Mã "+discount.getMemberType());
							productDiscounts.add(productDiscountDTO);
						}else {
							productDiscountDTO.setDiscount(discount);
							productDiscountDTO.setAble(false); 
							productDiscountDTO.setReason("Giá trị đơn hàng chưa đủ điều kiện áp dụng mã này");
							productDiscounts.add(productDiscountDTO);	
						}									
				}
			}
		}
		return new BillUpdateDiscountDTO(productDiscounts);
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
