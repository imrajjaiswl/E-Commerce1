
package com.kodewala.ecommerce.main;

import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.ProductRepository;

public class ProductRepositoryTest {

	public static void main(String[] args) {

		// Create repository object
		ProductRepository repository = new ProductRepository();

		// Create product objects
		Product p1 = new Product(101, "iPhone 16", "Mobile", 79999.0, 10, "Apple");

		Product p2 = new Product(102, "Galaxy S25", "Mobile", 69999.0, 15, "Samsung");

		// Add products
		repository.addProduct(p1);
		repository.addProduct(p2);

		// Display all products
		System.out.println("All Products:");

		repository.getAllProducts().forEach(System.out::println);

		// Search product
		System.out.println("\nSearching Product:");

		Product product = repository.findProductById(102);

		if (product != null) {
			System.out.println(product);
		} else {
			System.out.println("Product not found");
		}

		// Delete product
		boolean deleted = repository.deleteProduct(101);

		System.out.println("\nProduct Deleted: " + deleted);

		// Display remaining products
		System.out.println("\nRemaining Products:");

		repository.getAllProducts().forEach(System.out::println);
	}
}
