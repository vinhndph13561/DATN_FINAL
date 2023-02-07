package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.BillDTO;
import com.example.demo.dto.BillDetailDTO;
import com.example.demo.dto.QuantityProduct;
import com.example.demo.entities.Bill;
import com.example.demo.entities.BillDetail;
import com.example.demo.entities.Delivery;
import com.example.demo.entities.User;
import com.example.demo.excelexporter.BillExcelExporter;
import com.example.demo.repository.BillDetailRepository;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.DeliveryRespository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MappingBillDTOService;
import com.example.demo.service.MappingBillDetailDTOService;
import com.example.demo.service.impl.BillServiceImp;

@Controller
public class BillDetailsController {

	@Autowired
	MappingBillDTOService mappingbilldtoRepository;

	@Autowired
	MappingBillDetailDTOService mappingbilldetaildtoRepository;

	@Autowired
	DeliveryRespository deliveriesRepository;

	@Autowired
	BillRepository billRepository;

	@Autowired
	BillDetailRepository billDetailRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BillServiceImp billService;

	@RequestMapping("admin/billByStatus0/list")
	public String listBillByStatus0(Model model) {
		List<BillDTO> lstBillByisReturned0 = mappingbilldtoRepository.getBillDTOByStatus(0);
		model.addAttribute("listBill", lstBillByisReturned0);
		return "admin/bill/tables_billchuaduyet";
	}

	@RequestMapping("admin/billByStatus1/list")
	public String listBillByStatus1(Model model) {
		List<BillDTO> lstBillByisReturned1 = mappingbilldtoRepository.getBillDTOByStatus(1);
		model.addAttribute("listBill", lstBillByisReturned1);
		return "admin/bill/tables_billdaduyet";
	}

	@RequestMapping("/admin/billdetail/list/{id}")
	public String listBillDetailByBillId(@PathVariable("id") Long id, Model model, HttpSession session) {
		Bill bill = billRepository.getById(id);
		List<BillDetailDTO> lstBillDetail = mappingbilldetaildtoRepository.getAllBillDetailByBillDTO(id);
		User user = userRepository.getById(bill.getCustomer().getId());
		model.addAttribute("listBillDetail", lstBillDetail);
		Delivery delivery = deliveriesRepository.findByBillIdEquals(bill.getId());
		model.addAttribute("user", user);
		model.addAttribute("bill", bill);
		model.addAttribute("total", (int) bill.getTotal());
		model.addAttribute("delivery", delivery);
		return "admin/bill/tables_billDetails";
	}

	@RequestMapping("/api/billanddelivery/update/{id}")
	public String updateStatus1(@PathVariable("id") Long id, Model model) {
		Delivery _deliveryExisting = deliveriesRepository.getById(id);
		_deliveryExisting.setStatus(1);
		deliveriesRepository.save(_deliveryExisting);
		Bill _billExisting = billRepository.getById(_deliveryExisting.getBill().getId());
		_billExisting.setStatus(1);
		billRepository.save(_billExisting);
		return "redirect:/admin/billanddelivery/update/success";
	}

	@RequestMapping("/admin/billanddelivery/update/success")
	public String listBillByStatus1Success(Model model) {
		List<BillDTO> lstBillByisReturned1 = mappingbilldtoRepository.getBillDTOByStatus(1);
		model.addAttribute("listBill", lstBillByisReturned1);
		model.addAttribute("updateSuccess", "Duyệt đơn hàng thành công!");
		return "admin/bill/tables_billdaduyet";
	}

	@RequestMapping("admin/billByStatus2/list")
	public String listBillByStatus2(Model model) {
		List<BillDTO> lstBillByisReturned2 = mappingbilldtoRepository.getBillDTOByStatus(2);
		model.addAttribute("listBill", lstBillByisReturned2);
		return "admin/bill/tables_billdanhan";
	}

	@RequestMapping("admin/billByStatus3/list")
	public String listBillByStatus3(Model model) {
		List<BillDTO> lstBillByisReturned3 = mappingbilldtoRepository.getBillDTOByStatus(3);
		model.addAttribute("listBill", lstBillByisReturned3);
		return "admin/bill/tables_billbihuy";
	}

	@RequestMapping("admin/billByStatus4/list")
	public String listBillByStatus4(Model model) {
		List<BillDTO> lstBillByisReturned4 = mappingbilldtoRepository.getBillDTOByStatus(4);
		model.addAttribute("listBill", lstBillByisReturned4);
		return "admin/bill/tables_billbihoan";
	}

	@RequestMapping(value = "admin/updateStatusBill/list", method = RequestMethod.POST)
	public String listUpdateStatusBill(@RequestParam("id") ArrayList<Long> id) {
		for (Long billId : id) {
			Bill _billExisting = billRepository.getById(billId);
			String id2 = String.valueOf(billId);
			String status = billService.getDeliveryOrderDetail(id2);
			System.out.println(status);
			if (status.equals("ready_to_pick") || status.equals("picking") || status.equals("money_collect_picking")
					|| status.equals("picked") || status.equals("storing") || status.equals("transporting")) {
				continue;
			} else if (status.equals("delivered")) {
				_billExisting.setStatus(2);
				billRepository.save(_billExisting);
			} else {
				_billExisting.setStatus(4);
				billRepository.save(_billExisting);
			}
		}

		return "redirect:/admin/billanddelivery/update/success";
	}

	@RequestMapping(value = "/admin/billByThongKe/list", method = RequestMethod.POST)
	public String billByThongKe(Model model, @RequestParam("date1") String date1, @RequestParam("date2") String date2) {
		try {
			Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
			Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
			System.out.println(date3);
			System.out.println(date4);
			model.addAttribute("billtotal", billRepository.findBillTotalByCreateDay(date3, date4));
			model.addAttribute("billcount", billRepository.findBillCountByCreateDay(date3, date4));
			List<BillDTO> lstBillByCreateDay = mappingbilldtoRepository.findBillDTOByCreateDay(date3, date4);
			model.addAttribute("listBill", lstBillByCreateDay);
			List<String> lstQuantityProductByCreateDay = (List<String>) billDetailRepository.findQuantityProductByCreateDay(date3,
					date4);
			if (lstQuantityProductByCreateDay.size()>10) {
				lstQuantityProductByCreateDay = lstQuantityProductByCreateDay.subList(0, 9);
			}
			List<QuantityProduct> list = new ArrayList<>();
			for (String object : lstQuantityProductByCreateDay) {
				System.out.println(object.split(","));
				String a[] = object.split(",");		
				System.out.println(a[0]);
				
				QuantityProduct quantityProduct = new QuantityProduct();
				quantityProduct.setProductName(a[0]);
				quantityProduct.setQUantity(a[1]);
				list.add(quantityProduct);
			}
			model.addAttribute("listProduct", list);
			return "admin/trangchu";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/admin/index/faleid";
		}

	}

	@RequestMapping("/admin/index/faleid")
	public String adminfaleid(Model model) {
		model.addAttribute("updateFailed", "Không đơn hàng nào!");
		return "admin/trangchu";
	}

	@RequestMapping("/admin/index")
	public String admin1() {
		return "admin/trangchu";
	}

	/***
	 * Export data to excel file
	 */
	@RequestMapping(value = "/admin/bill/excelExport", method = RequestMethod.GET)
	public ModelAndView exportToExcel() {
		ModelAndView mav = new ModelAndView();
		mav.setView(new BillExcelExporter());
		// read data from DB
		List<BillDTO> list = mappingbilldtoRepository.getAllBillDTO();
		// send to excelImpl class
		mav.addObject("list", list);
		return mav;
	}
}
