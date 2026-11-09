
package com.kodewala.ecommerce.service;

import java.util.List;

import com.kodewala.ecommerce.model.Product;

public interface ProductService {

	void addProduct(Product product);

	List<Product> getAllProducts();

	Product getProductById(int productId);

	void deleteProduct(int productId);

	List<Product> searchProductsByName(String productName);

	List<Product> searchProductsByCategory(String category);

	List<Product> searchProductsByBrand(String brand);

	List<Product> searchProductsByPriceRange(double minPrice, double maxPrice);

	void updateProductPrice(int productId, double newPrice);

	void updateProductQuantity(int productId, int newQuantity);

	List<Product> sortProductsByPriceAscending();

	List<Product> sortProductsByPriceDescending();
}
