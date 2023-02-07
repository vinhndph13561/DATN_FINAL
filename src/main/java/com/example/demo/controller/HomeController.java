package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entities.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.ProductServiceImp;

@Controller
public class HomeController {

	@Autowired
	private ProductServiceImp productServiceImp;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository cateRepository;

	@RequestMapping({ "/", "/home/index" })
	public String home(Model model, Principal principal) {
		return "customer/test";
	}

	@RequestMapping({ "/admin", "/admin/home/index" })
	public String admin() {
		return "redirect:/assets/admin/index.html";
	}

	@RequestMapping({ "/products" })
	public String products() {
		return "customer/product";
	}

	@RequestMapping({ "/history" })
	public String history() {
		return "customer/history";
	}

	@RequestMapping({ "/details" })
	public String detail() {
		return "customer/chitiet-sanpham";
	}

	@RequestMapping({ "/contact" })
	public String contact() {
		return "customer/lienhe";
	}

	@RequestMapping({ "/detail-news" })
	public String detailnews() {
		return "customer/detail-news";
	}

	@RequestMapping({ "/tintuc" })
	public String tintuc() {
		return "customer/tintuc";
	}

	@RequestMapping({ "/carts" })
	public String cart() {
		return "customer/cart";
	}

	@RequestMapping({ "/productdetail" })
	public String prodetail() {
		return "customer/productdetail";
	}
}
