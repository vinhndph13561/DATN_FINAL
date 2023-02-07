package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.InventoryProduct;
import com.example.demo.entities.InventoryProductDetail;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductDetail;

@Repository
public interface InventoryProductDetailRepository extends JpaRepository<InventoryProductDetail, Long> {
	@Query("SELECT pd FROM InventoryProductDetail pd WHERE pd.product = :product")
	List<InventoryProductDetail> findByProduct(@Param("product") Product product);

	public List<InventoryProductDetail> findByProductIdEquals(Long id);

	@Query("SELECT pd FROM ProductDetail pd WHERE pd.product = :product and size = :size and color = :color")
	InventoryProductDetail findByProductDetailNameSizeColorEquals(@Param("product") Product product, String size, String color);

	InventoryProductDetail findBySizeAndColorAndProduct(String size, String color, InventoryProduct product);

	InventoryProductDetail findByColorAndSizeAndProductId(String color, String size, Long id);

	List<InventoryProductDetail> findByColorAndProductId(String color, Long id);

}
