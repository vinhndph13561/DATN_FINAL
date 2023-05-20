package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.AddProductDetailDTO;
import com.example.demo.entities.InventoryProduct;
import com.example.demo.entities.InventoryProductDetail;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductDetail;
import com.example.demo.entities.User;
import com.example.demo.repository.InventoryProductDetailRepository;
import com.example.demo.repository.InventoryProductReponsitory;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class InventoryControler {
	@Autowired
	InventoryProductReponsitory inventoryProductReponsitory;
	@Autowired
	InventoryProductDetailRepository inventoryProductDetailRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProductDetailRepository productDetailRepository;
	@Autowired
	UserRepository userRepository;

	@GetMapping("/inventory")
	public String inventory(ModelMap map) {
		List<InventoryProduct> list = inventoryProductReponsitory.findAll();
		map.addAttribute("inventory", list);
		return "admin/product/tables_inventorys";
	}

	@GetMapping("/inventoryDetail/view/{id}")
	public String viewInventorydetail(ModelMap map, @PathVariable(value = "id") Long id) {
//		List<InventoryProductDetail> list = inventoryProductDetailRepository.findAllById(id);
		InventoryProduct inventoryProduct = inventoryProductReponsitory.getById(id);
		System.out.println(inventoryProduct.getPrice());
//		map.addAttribute("inventoryDetail",inventoryProductDetailRepository.findByProductIdEquals(id));
		map.addAttribute("cost", inventoryProduct.getPrice());
		List<InventoryProductDetail> list = inventoryProductDetailRepository.findByProductIdEquals(id);
		List<AddProductDetailDTO> listProductDetails = new ArrayList<>();
		for (InventoryProductDetail detail : list) {
//			List<Double> list1 = new ArrayList<Double>();
			AddProductDetailDTO addProductDetailDTO = new AddProductDetailDTO();
			if (productRepository.findByName(detail.getProduct().getName()) != null) {
				Product product = productRepository.findByName(detail.getProduct().getName());
				if (productDetailRepository.findBySizeAndColorAndProduct(detail.getSize(), detail.getColor(),
						product) != null) {
					//bo sung else
					ProductDetail productDetail = productDetailRepository.findBySizeAndColorAndProduct(detail.getSize(),
							detail.getColor(), product);
//					list1.add(productDetail.getPrice());
					addProductDetailDTO.setInventoryProductDetail(detail);
					addProductDetailDTO.setPrice(productDetail.getPrice());
					listProductDetails.add(addProductDetailDTO);
				} else {
					addProductDetailDTO.setInventoryProductDetail(detail);
//					addProductDetailDTO.setPrice(productDetail.getPrice());
					listProductDetails.add(addProductDetailDTO);
				}
			} else {
				addProductDetailDTO.setInventoryProductDetail(detail);
//				addProductDetailDTO.setPrice(productDetail.getPrice());
				listProductDetails.add(addProductDetailDTO);
			}
		}
		map.addAttribute("inventoryDetail", listProductDetails);
		return "admin/product/tables_inventoryDetail";
	}

	@PostMapping("/addProduct")
	public String addProduct(ModelMap map,@RequestParam("chk[]") Long[] chk,@RequestParam("chk2") Long[] chk2,@RequestParam("listPD") ArrayList<Double> prices, @RequestParam("listPD1") ArrayList<Integer> quan,Principal principal) {
		try {
		User user = userRepository.findByUsernameEquals(principal.getName());
		for (int i= 0; i<chk.length; i++ ) {
//			List<InventoryProductDetail> inventoryProductDetails = inventoryProductDetailRepository.findByProduct(prs[i]);
			Product product = new Product();
//			System.out.println(prs[i]);
			
			 InventoryProductDetail inventoryProductDetail = inventoryProductDetailRepository.getById(chk[i]);
			 InventoryProduct inventoryProduct = inventoryProductReponsitory.getById(inventoryProductDetail.getProduct().getId());
			 if (productRepository.findByName(inventoryProductDetail.getProduct().getName()) == null) {
				 product.setCategory(inventoryProduct.getCategory());
				 product.setCreateDay(new Date());
				 product.setCreatedBy(user.getId());
				 product.setImage(inventoryProduct.getImage());
				 product.setMaterial(inventoryProduct.getMaterial());
				 product.setModifiedBy(user.getId());
				 product.setModifyDay(new Date());
				 product.setName(inventoryProduct.getName());
				 product.setNote(inventoryProduct.getNote());
				 product.setPrice(inventoryProduct.getPrice());
				 product.setStatus(1);
				 productRepository.save(product);
			 }
			 if (productRepository.findByName(inventoryProductDetail.getProduct().getName()) != null) {
				 product = productRepository.findByName(inventoryProductDetail.getProduct().getName());
			 }
			 if (productDetailRepository.findBySizeAndColorAndProduct(inventoryProductDetail.getSize(), inventoryProductDetail.getColor(), product) == null) {
				 ProductDetail productDetail = new ProductDetail();
				 productDetail.setColor(inventoryProductDetail.getColor());
				 productDetail.setCreateDay(new Date());
				 productDetail.setCreatedBy(user.getId());
				 productDetail.setModifiedBy(user.getId());
				 productDetail.setModifyDay(new Date());
				 productDetail.setProduct(product);
				  //set quantity
				 for(int j=0;j<chk2.length;j++) {
					 if (chk2[j] == chk[i]) {
						 //chet vi null
						 if (productDetail.getQuantity() != null) {
						 productDetail.setQuantity(productDetail.getQuantity() + quan.get(j));
						 } else {
							 productDetail.setQuantity(quan.get(j));
						 }
						 if (inventoryProductDetail.getQuantity() != null) {
						 inventoryProductDetail.setQuantity(inventoryProductDetail.getQuantity() - quan.get(j));
						 } else {
							 inventoryProductDetail.setQuantity(quan.get(j));
						 }
						 if (prices.get(j) != 0) {
							 productDetail.setPrice(prices.get(j));
						 } else {
							 if (inventoryProductDetail.getSize().equals("XXS") || inventoryProductDetail.getSize().equals("XS")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice() * 1.3);
							 } else if (inventoryProductDetail.getSize().equals("S")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice() * 1.3 +5000);
							 } else if (inventoryProductDetail.getSize().equals("M")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice() * 1.3 + 5000);
							 } else if (inventoryProductDetail.getSize().equals("L")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice()  * 1.3 +10000);
							 } else if (inventoryProductDetail.getSize().equals("XL")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice() * 1.3 +10000);
							 } else if (inventoryProductDetail.getSize().equals("XXL")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice() * 1.3 + 15000);
							 }else if (inventoryProductDetail.getSize().equals("XXXL")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice() * 1.3 + 15000);
							 }
						 }
					}
				 }
				 
				 productDetail.setSize(inventoryProductDetail.getSize());
				 productDetail.setStatus(1);
				 productDetail.setThumnail(inventoryProductDetail.getThumnail());
				 productDetail.setBareCode(1234567);
				 
				 
				 productDetailRepository.save(productDetail);
				 
			 } else {
				 ProductDetail productDetail = productDetailRepository
						 .findBySizeAndColorAndProduct(inventoryProductDetail.getSize(), inventoryProductDetail.getColor(), product);
				 productDetail.setModifiedBy(user.getId());
				 productDetail.setModifyDay(new Date());
				 for(int j=0;j<chk2.length;j++) {
					 if (chk2[j] == chk[i]) {
						 productDetail.setQuantity( productDetail.getQuantity() + quan.get(j));
						 inventoryProductDetail.setQuantity(inventoryProductDetail.getQuantity() - quan.get(j));
						 if (prices.get(j) != 0) {
							 productDetail.setPrice(prices.get(j));
						 } else {
							 if (inventoryProductDetail.getSize().equals("XXS") || inventoryProductDetail.getSize().equals("XS")) {
								 productDetail.setPrice(productDetail.getProduct().getPrice() * 1.3);
							 } else if (inventoryProductDetail.getSize().equals("S") || inventoryProductDetail.getSize().equals("M")) {
								 productDetail.setPrice((productDetail.getProduct().getPrice() * 1.3) + 5000);
							 } else if (inventoryProductDetail.getSize().equals("L") || inventoryProductDetail.getSize().equals("XL")) {
								 productDetail.setPrice((productDetail.getProduct().getPrice() * 1.3) + 10000);
							 } else if (inventoryProductDetail.getSize().equals("XXL") || inventoryProductDetail.getSize().equals("XXXL")) {
								 productDetail.setPrice((productDetail.getProduct().getPrice()  * 1.3) + 15000);
							 }
						 }
					}
				 }
				 productDetail.setBareCode(1234561);
				 productDetailRepository.save(productDetail);
				 
			 }
//			 ProductDetail productDetail = productRepository.getById(prs[i]);
//			 if (productRepository.findByName(null))
//			 inventoryProductDetail.setPrice(prices.get(i));
//			 inventoryProductDetailRepository.save(inventoryProductDetail);
		}
		map.addAttribute("insertSuccess", "nhập hàng thành công!");
		return "redirect:/inventory/succses";
	} catch (Exception e) {
		e.printStackTrace();
		map.addAttribute("insertFailed", "nhập hàng thất bại!");
		return "redirect:/inventory/failed";
	}
	}
	
	@GetMapping("/inventory/succses")
	public String inventorysucces(ModelMap map) {
		List<InventoryProduct> list = inventoryProductReponsitory.findAll();
		map.addAttribute("inventory", list);
		map.addAttribute("insertSuccess", "nhập hàng thành công!");
		return "admin/product/tables_inventorys";
	}
	
	@GetMapping("/inventory/failed")
	public String inventoryfailed(ModelMap map) {
		List<InventoryProduct> list = inventoryProductReponsitory.findAll();
		map.addAttribute("inventory", list);
		map.addAttribute("insertFailed", "nhập hàng thất bại!");
		return "admin/product/tables_inventorys";
	}
}
