package com.example.demo.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.BillDetail;
import com.example.demo.repository.BillDetailRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.BillServiceImp;



@Controller
@RequestMapping("/api/billdetails")
public class BillDetailController {
	@Autowired
    BillServiceImp billDetailService;
	
	@Autowired
    BillDetailRepository billDetailRepository;
	
    @Autowired
    UserRepository userRepository;

    //view bill Details
    @GetMapping("/billdetail/{billId}")
    @ResponseBody
    public List<BillDetail> billDetail(@PathVariable("billId") Long billId) {
        List<BillDetail> billDetail = billDetailService.findBillDetailByBillId(billId);
        return billDetail;
    }
    
    @GetMapping("/waitforreturn")
    public String getWait(Model model) {
        List<BillDetail> billDetail = billDetailRepository.findByStatusEquals("Chờ hoàn");
        model.addAttribute("listBill", billDetail);
        return "admin/bill/return_bill";
    }
    
    @GetMapping("/waitforreturn/check/{billDetailId}")
    public String checkWait(Model model,@PathVariable("billDetailId") Long billDetailId) {
    	BillDetail billDetail = billDetailRepository.getById(billDetailId);
    	billDetail.setStatus("Đã hoàn");
    	billDetailRepository.save(billDetail);
        List<BillDetail> billDetails = billDetailRepository.findByStatusEquals("Chờ hoàn");
        model.addAttribute("listBill", billDetails);
        model.addAttribute("updateSuccess", "Duyệt đơn hàng thành công!");
        return "admin/bill/return_bill";
    }
    
    @GetMapping("/waitforreturn/cancel/{billDetailId}")
    public String cancelWait(Model model,@PathVariable("billDetailId") Long billDetailId) {
    	BillDetail billDetail = billDetailRepository.getById(billDetailId);
    	billDetail.setStatus("Đã nhận");
    	billDetailRepository.save(billDetail);
        List<BillDetail> billDetails = billDetailRepository.findByStatusEquals("Chờ hoàn");
        model.addAttribute("listBill", billDetails);
        model.addAttribute("updateSuccess", "Hủy yêu cầu thành công!");
        return "admin/bill/return_bill";
    }
}
