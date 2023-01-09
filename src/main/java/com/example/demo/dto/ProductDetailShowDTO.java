package com.example.demo.dto;

import java.util.List;

import com.example.demo.entities.Interaction;
import com.example.demo.entities.ProductDetail;

public class ProductDetailShowDTO {
	private ProductShow product;
	
<<<<<<< HEAD
=======
	private List<ProductDetail> productDetails;
	
>>>>>>> 35b31b0f3530a1aca3db6c66a20490c83f77ed7b
	private List<ColorDTO> colors;
	
	private List<SizeDTO> sizes;
	
	private List<ProductShow> relatedProducts;
	
	private List<Interaction> interactions;
	
<<<<<<< HEAD
	private List<ProductDiscountDTO> productDiscounts;

	public ProductDetailShowDTO(ProductShow product, List<ColorDTO> colors, List<SizeDTO> sizes,
			List<ProductShow> relatedProducts, List<Interaction> interactions,
			List<ProductDiscountDTO> productDiscounts) {
		super();
		this.product = product;
=======
	

	public ProductDetailShowDTO(ProductShow product, List<ProductDetail> productDetails, List<ColorDTO> colors,
			List<SizeDTO> sizes, List<ProductShow> relatedProducts, List<Interaction> interactions) {
		super();
		this.product = product;
		this.productDetails = productDetails;
>>>>>>> 35b31b0f3530a1aca3db6c66a20490c83f77ed7b
		this.colors = colors;
		this.sizes = sizes;
		this.relatedProducts = relatedProducts;
		this.interactions = interactions;
<<<<<<< HEAD
		this.productDiscounts = productDiscounts;
	}

	public List<ProductDiscountDTO> getProductDiscounts() {
		return productDiscounts;
	}

	public void setProductDiscounts(List<ProductDiscountDTO> productDiscounts) {
		this.productDiscounts = productDiscounts;
	}

=======
	}
	
>>>>>>> 35b31b0f3530a1aca3db6c66a20490c83f77ed7b
	public List<Interaction> getInteractions() {
		return interactions;
	}

	public void setInteractions(List<Interaction> interactions) {
		this.interactions = interactions;
	}

	public ProductShow getProduct() {
		return product;
	}

	public void setProduct(ProductShow product) {
		this.product = product;
	}

<<<<<<< HEAD
=======
	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}

>>>>>>> 35b31b0f3530a1aca3db6c66a20490c83f77ed7b
	public List<ColorDTO> getColors() {
		return colors;
	}

	public void setColors(List<ColorDTO> colors) {
		this.colors = colors;
	}

	public List<SizeDTO> getSizes() {
		return sizes;
	}

	public void setSizes(List<SizeDTO> sizes) {
		this.sizes = sizes;
	}

	public List<ProductShow> getRelatedProducts() {
		return relatedProducts;
	}

	public void setRelatedProducts(List<ProductShow> relatedProducts) {
		this.relatedProducts = relatedProducts;
	}
	
	
}
