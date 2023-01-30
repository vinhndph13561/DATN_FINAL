package com.example.demo.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.config.PaypalPaymentIntent;
import com.example.demo.config.PaypalPaymentMethod;
import com.example.demo.entities.Bill;
import com.example.demo.entities.Delivery;
import com.example.demo.entities.Discount;
import com.example.demo.entities.User;
import com.example.demo.repository.DeliveryRespository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.BillServiceImp;
import com.example.demo.service.impl.DiscountServiceImp;
import com.example.demo.service.impl.PaypalService;
import com.example.demo.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaymentController {

	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private PaypalService paypalService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BillServiceImp billService;
	
	@Autowired
	private DeliveryRespository deliveryRespository;
	
	@Autowired
	private DiscountServiceImp discountServiceImp;

	@RequestMapping(value = "/payMoney", method = RequestMethod.POST)
	@ResponseBody
	public String pay(HttpServletRequest request, @RequestParam("price") double price,
			@RequestParam("address") String address,
			@RequestParam("ward") String ward,
			@RequestParam("district") String district,
			@RequestParam("province") String province,
			@RequestParam("services") Integer services,
			@RequestParam("note") @Nullable String note,
			@RequestParam("decrease") @Nullable Double decrease,
			@RequestParam("discountId") @Nullable Long discountId,
			@RequestParam("paymentType") String type,  
			@RequestParam(name = "user") @Nullable Integer userId) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}else {
			return "Bạn cần đăng nhập";
		}
		if (discountId != null) {
			boolean state = discountServiceImp.reductionDiscount(discountId);
			if (state == false) {
				return "Mã khuyến mãi bạn chọn đã hết";
			}
		}
		Bill bill = billService.saveBill(user, "Thanh toán khi nhận hàng",decrease);
		if (bill == null) {
			return "Sản phẩm bạn chọn hiện đã hết, vui lòng kiểm tra lại";
		}
		Delivery delivery = new Delivery();
		delivery.setAddress(address);
		delivery.setWard(ward);
		delivery.setDistrict(district);
		delivery.setProvince(province);
		delivery.setServices(services);
		delivery.setStatus(0);
		delivery.setBill(bill);
		delivery.setNote(note);
		deliveryRespository.save(delivery);
		if (type.equals("cash")) {
			return "Thành công";
		}
		
		String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
		try {
			Payment payment = paypalService.createPayment(price, "USD", PaypalPaymentMethod.paypal,
					PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return links.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "Thành công";
	}

	@RequestMapping(URL_PAYPAL_CANCEL)
	public String cancelPay() {
		return "cancel";
	}

	@RequestMapping(URL_PAYPAL_SUCCESS)
	@ResponseBody
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
			Principal principal) {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				return "Thành công";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
			
			return "Thanh toán thất bại";
		}
		return "Thanh toán thất bại";
	}

}
