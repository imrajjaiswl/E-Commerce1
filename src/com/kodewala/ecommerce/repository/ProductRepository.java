
package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;

import com.kodewala.ecommerce.model.Product;

public class ProductRepository {

	// Collection to store products
	private List<Product> products = new ArrayList<>();

	// Add product
	public void addProduct(Product product) {
		products.add(product);
	}

	// Get all products
	public List<Product> getAllProducts() {
		return new ArrayList<>(products);
	}

	// Find product by ID
	public Product findProductById(int productId) {

		for (Product product : products) {

			if (product.getProductId() == productId) {
				return product;
			}
		}

		return null;
	}

	// Delete product by ID
	public boolean deleteProduct(int productId) {

		Product product = findProductById(productId);

		if (product != null) {
			products.remove(product);
			return true;
		}

		return false;
	}
}
