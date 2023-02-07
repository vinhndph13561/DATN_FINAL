package com.example.demo.controller;

import java.security.Principal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.ProductListDTO;
import com.example.demo.dto.ProductShow;
import com.example.demo.entities.Category;
import com.example.demo.entities.Interaction;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductDetail;
import com.example.demo.entities.User;
import com.example.demo.repository.BillDetailRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.DiscountDetailRepository;
import com.example.demo.repository.InteractionRepository;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.ProductServiceImp;

@Controller
@RequestMapping("api/product")
public class ProductController {
	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private ProductServiceImp productService;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private InteractionRepository interactionRepository;

	@Autowired
	private DiscountDetailRepository discountDetailRepository;

	@Autowired
	private ProductDetailRepository productDetailRepo;

	@Autowired
	private BillDetailRepository billDetailRepository;

	@Autowired
	private UserRepository userRepository;

	// product Create
	@RequestMapping("create")
	public String create(Model model) {
		List<Category> list = categoryRepo.findAll();
		model.addAttribute("listCategory", list);
		return "product/create";
	}

	// create --> productDetail index
	@PostMapping("/store")
	public String store(@ModelAttribute("product") Product product, Model model) {
		Product p = product;
		this.productRepo.save(p);

		Product prod = productRepo.findByName(p.getName());
		model.addAttribute("product", product);

		List<ProductDetail> listPD = productDetailRepo.findByProduct(prod);
		model.addAttribute("listProductDetail", listPD);

		return "productDetail/index";
	}

	// update product --> productDetail index
//	@PostMapping("/update/{id}")
//	public String update(@ModelAttribute("product") Product product, Model model, @PathVariable("id") Long id) {
//		Product p = product;
//		p.setId(id);
//		this.productRepo.save(p);
//
//		Product prod = productRepo.findByName(p.getName());
//		model.addAttribute("product", product);
//
//		List<ProductDetail> listPD = productDetailRepo.findByProduct(prod);
//		model.addAttribute("listProductDetail", listPD);
//
//		return "productDetail/index";
//	}

	// edit product
	@RequestMapping("edit/{id}")
	public String editProduct(Model model, @PathVariable("id") Long id) {
		Product product = productRepo.findById(id).get();
		model.addAttribute("product", product);

		List<Category> list = categoryRepo.findAll();
		model.addAttribute("listCategory", list);

		return "product/edit";
	}

	// delete product
	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		Product p = productRepo.findById(id).get();
		List<ProductDetail> listProD = productDetailRepo.findByProduct(p);
		if (listProD.size() != 0) {
			for (ProductDetail proD : listProD) {
				this.productDetailRepo.deleteById(proD.getId());
			}
		}
		this.productRepo.deleteById(id);
		return "redirect:/product/index";
	}

	// index product
	@GetMapping("all")
	@ResponseBody
	public ProductListDTO index(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "user") @Nullable Integer userId) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}
		
		List<Category> list = categoryRepo.findAll();

		Pageable pageable = PageRequest.of(page, size);
		List<Product> products = productRepo.findByStatusEquals(1);
		Page<ProductShow> data = productService.getPageProduct(products,pageable, user);
		
		Set<String> colors = new LinkedHashSet<String>(productDetailRepo.findAllColor());
		
		return new ProductListDTO(data,list,colors);
	}

	@GetMapping("indexbycate")
	@ResponseBody
	public ProductListDTO indexByCate(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "user") @Nullable Integer userId, @RequestParam(name = "id") Integer id) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}
		List<Category> list = categoryRepo.findAll();

		Pageable pageable = PageRequest.of(page, size);
		List<Product> products = productRepo.findByCategoryIdAndStatus(id,1);
		Page<ProductShow> data = productService.getPageProduct(products,pageable, user);
		
		Set<String> colors = new LinkedHashSet<String>(productDetailRepo.findAllColor());
		
		return new ProductListDTO(data,list,colors);
	}

	@GetMapping("userLike")
	@ResponseBody
	public ProductListDTO getUserLike(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "user") @Nullable Integer userId) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}
		
		List<Category> list = categoryRepo.findAll();

		Pageable pageable = PageRequest.of(page, size);
		List<Product> products = interactionRepository.findByUserLike2(user);
		Page<ProductShow> data = productService.getPageProduct(products,pageable, user);
		
		Set<String> colors = new LinkedHashSet<String>(productDetailRepo.findAllColor());
		
		return new ProductListDTO(data,list,colors);
	}

	@GetMapping("discountProduct")
	@ResponseBody
	public ProductListDTO getDisCountProduct(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "user") @Nullable Integer userId) {
		
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}
		
		List<Category> list = categoryRepo.findAll();

		Pageable pageable = PageRequest.of(page, size);
		List<Product> products = discountDetailRepository.findByDiscountEndDayAfter2(new Date());
		Page<ProductShow> data = productService.getPageProduct(products,pageable, user);
		
		Set<String> colors = new LinkedHashSet<String>(productDetailRepo.findAllColor());
		
		return new ProductListDTO(data,list,colors);
	}

	@GetMapping("maxInter")
	@ResponseBody
	public ProductListDTO getMaxInter(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "user") @Nullable Integer userId) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}
		
		List<Category> list = categoryRepo.findAll();

		Pageable pageable = PageRequest.of(page, size);
		List<Product> products = productService.getAllProductByInteraction();
		Page<ProductShow> data = productService.getPageProduct(products,pageable, user);
		
		Set<String> colors = new LinkedHashSet<String>(productDetailRepo.findAllColor());
		
		return new ProductListDTO(data,list,colors);
		
	}

	@GetMapping("maxBuy")
	@ResponseBody
	public ProductListDTO getMaxBuy(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "user") @Nullable Integer userId) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}
		
		List<Category> list = categoryRepo.findAll();

		Pageable pageable = PageRequest.of(page, size);
		List<Product> products = productService.getAllProductByBuyQuantity();
		Page<ProductShow> data = productService.getPageProduct(products,pageable, user);
		
		Set<String> colors = new LinkedHashSet<String>(productDetailRepo.findAllColor());
		
		return new ProductListDTO(data,list,colors);
			
	}

	@GetMapping("index2")
	public String index2(Model model, @ModelAttribute("product") Product product,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "20") Integer size) {
		model.addAttribute("create", true);
		List<Category> list = categoryRepo.findAll();
		model.addAttribute("listCategory", list);

		Pageable pageable = PageRequest.of(page, size);
		Page<ProductDetail> data = this.productDetailRepo.findAll(pageable);

		model.addAttribute("data", data);
		return "customer/product";
	}

	// Product Detail

	// create productDetail
	@PostMapping("/productDetail/store/{id}")
	public String storeDetail(@ModelAttribute("productDetail") ProductDetail productDetail, Model model,
			@PathVariable("id") Long id) {
		ProductDetail proDt = productDetail;

		Product product = productRepo.findById(id).get();
		proDt.setProduct(product);

		this.productDetailRepo.save(proDt);

		model.addAttribute("product", product);

		List<ProductDetail> listPD = productDetailRepo.findByProduct(product);
		model.addAttribute("listProductDetail", listPD);
		model.addAttribute("buyquantity", billDetailRepository.findQuantityByProduct(product));
		model.addAttribute("listProduct", productRepo.findByCategory(product.getCategory()));
		return "customer/productdetail";
	}

	// productDetail index
	
	// delete productDetail
	@GetMapping("/productDetail/delete/{id}")
	public String deleteDetail(@ModelAttribute("productDetail") ProductDetail productDetail, Model model,
			@PathVariable("id") Long id) {
		ProductDetail proDt = productDetailRepo.getById(id);
		Product product = proDt.getProduct();
		model.addAttribute("product", product);
		productDetailRepo.deleteById(id);

		List<ProductDetail> listPD = productDetailRepo.findByProduct(product);
		model.addAttribute("listProductDetail", listPD);

		return "productDetail/index";
	}

	@PutMapping("/addLike/{id}")
	public boolean addLike(@PathVariable("id") Long id, Principal principal) {
		if (principal == null) {
			return false;
		}
		User user = userRepository.findByUsernameEquals(principal.getName());
		Product product = productRepo.findById(id).get();
		productService.addLikeProduct(user, product);
		return true;
	}

	@GetMapping("/getList")
	@ResponseBody
	public List<ProductDetail> find(@RequestParam("id") Long id) {
		return productRepo.findById(id).get().getProductDetails();
	}

	@GetMapping("/getSize")
	@ResponseBody
	public List<ProductDetail> find(@RequestParam("color") String color, @RequestParam("proId") Long id) {
		return productDetailRepo.findByColorAndProductId(color, id);
	}

	@PostMapping("/filters")
	@ResponseBody
	public ProductListDTO findByFilter(@RequestParam("cateName") @Nullable String cateName,
						@RequestParam("material") @Nullable String material,
						@RequestParam("color") @Nullable String color,
						@RequestParam("size") @Nullable String size,
						@RequestParam("min") @Nullable Double min,
						@RequestParam("max") @Nullable Double max,
						@RequestParam("order") @Nullable String order,
						@RequestParam("rating") @Nullable Double rating,
						@RequestParam(name = "page", defaultValue = "0") Integer page,
						@RequestParam(name = "sizepage", defaultValue = "24") Integer sizepage,
						@RequestParam(name = "user") @Nullable Integer userId) {
			
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).get();
		}
		List<Category> list = categoryRepo.findAll();

		Pageable pageable = PageRequest.of(page, sizepage);
		List<Product> products = productService.getProductByFilter(cateName, material, color, size, order, min, max, rating);
		Page<ProductShow> data = productService.getPageProduct(products,pageable, user);
		
		Set<String> colors = new LinkedHashSet<String>(productDetailRepo.findAllColor());
		
		return new ProductListDTO(data,list,colors);
	}
	
	@PostMapping("/addlike")
	@ResponseBody
	public boolean addLike(@RequestParam("product_id") Long id,@RequestParam(name = "user") @Nullable Integer userId) {
		Interaction inter =  interactionRepository.findByUserIdAndProductId(userId,id);
		if (inter == null) {
			Interaction inter2 = new Interaction();
			inter2.setProduct(productRepo.getById(id));
			inter2.setUser(userRepository.getById(userId));
			inter2.setLikeStatus(1);
			inter2.setCreateTime(new Date());
			inter2.setComment("");
			inter2.setStatus(true);
			interactionRepository.save(inter2);
			return true;
		}
		if (inter.getLikeStatus()==0) {
			inter.setLikeStatus(1);
			interactionRepository.save(inter);
		}
		if (inter.getLikeStatus()==1) {
			inter.setLikeStatus(0);
			interactionRepository.save(inter);
		}
		return true;
	}
}
