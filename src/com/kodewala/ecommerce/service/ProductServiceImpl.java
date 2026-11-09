
package com.kodewala.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import com.kodewala.ecommerce.exception.InvalidQuantityException;
import com.kodewala.ecommerce.exception.ProductNotFoundException;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.ProductRepository;

public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	// Constructor injection
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// Add product
	@Override
	public void addProduct(Product product) {

		if (product == null) {
			throw new IllegalArgumentException("Product cannot be null");
		}

		if (product.getProductId() <= 0) {
			throw new IllegalArgumentException("Product ID must be greater than zero");
		}

		if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
			throw new IllegalArgumentException("Product name cannot be empty");
		}

		if (product.getPrice() <= 0) {
			throw new IllegalArgumentException("Product price must be greater than zero");
		}

		if (product.getQuantity() < 0) {
			throw new InvalidQuantityException("Product quantity cannot be negative");
		}

		if (productRepository.findProductById(product.getProductId()) != null) {
			throw new IllegalArgumentException("Product ID already exists");
		}

		productRepository.addProduct(product);

		System.out.println("Product added successfully");
	}

	// Get all products
	@Override
	public List<Product> getAllProducts() {
		return productRepository.getAllProducts();
	}

	// Get product by ID
	@Override
	public Product getProductById(int productId) {

		Product product = productRepository.findProductById(productId);

		if (product == null) {
			throw new ProductNotFoundException("Product with ID " + productId + " does not exist");
		}

		return product;
	}

	// Delete product
	@Override
	public void deleteProduct(int productId) {

		getProductById(productId);

		productRepository.deleteProduct(productId);

		System.out.println("Product deleted successfully");
	}

	// Search by product name
	@Override
	public List<Product> searchProductsByName(String productName) {

		return productRepository.getAllProducts().stream()
				.filter(p -> p.getProductName().toLowerCase().contains(productName.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Search by category
	@Override
	public List<Product> searchProductsByCategory(String category) {

		return productRepository.getAllProducts().stream().filter(p -> p.getCategory().equalsIgnoreCase(category))
				.collect(Collectors.toList());
	}

	// Search by brand
	@Override
	public List<Product> searchProductsByBrand(String brand) {

		return productRepository.getAllProducts().stream().filter(p -> p.getBrand().equalsIgnoreCase(brand))
				.collect(Collectors.toList());
	}

	// Search by price range
	@Override
	public List<Product> searchProductsByPriceRange(double minPrice, double maxPrice) {

		if (minPrice < 0 || maxPrice < minPrice) {
			throw new IllegalArgumentException("Invalid price range");
		}

		return productRepository.getAllProducts().stream()
				.filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice).collect(Collectors.toList());
	}

	// Update product price
	@Override
	public void updateProductPrice(int productId, double newPrice) {

		if (newPrice <= 0) {
			throw new IllegalArgumentException("Price must be greater than zero");
		}

		Product product = getProductById(productId);

		product.setPrice(newPrice);

		System.out.println("Product price updated successfully");
	}

	// Update product quantity
	@Override
	public void updateProductQuantity(int productId, int newQuantity) {

		if (newQuantity < 0) {
			throw new InvalidQuantityException("Quantity cannot be negative");
		}

		Product product = getProductById(productId);

		product.setQuantity(newQuantity);

		System.out.println("Product quantity updated successfully");
	}

	// Sort products by price: Low to High
	@Override
	public List<Product> sortProductsByPriceAscending() {

		return productRepository.getAllProducts().stream()
				.sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice())).collect(Collectors.toList());
	}

	// Sort products by price: High to Low
	@Override
	public List<Product> sortProductsByPriceDescending() {

		return productRepository.getAllProducts().stream()
				.sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice())).collect(Collectors.toList());
	}

}
