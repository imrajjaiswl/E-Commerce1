
package com.kodewala.ecommerce.main;

import java.util.List;

import com.kodewala.ecommerce.exception.ProductNotFoundException;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.ProductRepository;
import com.kodewala.ecommerce.service.ProductService;
import com.kodewala.ecommerce.service.ProductServiceImpl;

public class ProductServiceTest {

	public static void main(String[] args) {

		// 1. Create repository object
		ProductRepository repository = new ProductRepository();

		// 2. Inject repository into service
		ProductService service = new ProductServiceImpl(repository);

		try {

			// 3. Add products
			service.addProduct(new Product(101, "iPhone 16", "Mobile", 79999, 10, "Apple"));

			service.addProduct(new Product(102, "Galaxy S25", "Mobile", 69999, 15, "Samsung"));

			service.addProduct(new Product(103, "Dell Laptop", "Electronics", 55000, 5, "Dell"));

			// 4. Display all products
			System.out.println("\n========== ALL PRODUCTS ==========");

			List<Product> products = service.getAllProducts();

			for (Product product : products) {
				System.out.println(product);
			}

			// 5. Search product by ID
			System.out.println("\n========== SEARCH BY ID ==========");

			Product product = service.getProductById(101);
			System.out.println(product);

			// 6. Search by category
			System.out.println("\n========== SEARCH BY CATEGORY ==========");

			List<Product> mobileProducts = service.searchProductsByCategory("Mobile");

			for (Product p : mobileProducts) {
				System.out.println(p);
			}

			// 7. Search by price range
			System.out.println("\n========== SEARCH BY PRICE ==========");

			List<Product> filteredProducts = service.searchProductsByPriceRange(50000, 80000);

			for (Product p : filteredProducts) {
				System.out.println(p);
			}

			// 8. Update product price
			service.updateProductPrice(101, 75000);

			// 9. Update product quantity
			service.updateProductQuantity(101, 20);

			// 10. Delete product
			service.deleteProduct(102);

			// 11. Display remaining products
			System.out.println("\n========== REMAINING PRODUCTS ==========");

			for (Product p : service.getAllProducts()) {
				System.out.println(p);
			}

		} catch (ProductNotFoundException e) {

			System.err.println("PRODUCT ERROR: " + e.getMessage());

		} catch (IllegalArgumentException e) {

			System.err.println("VALIDATION ERROR: " + e.getMessage());

		} catch (Exception e) {

			System.err.println("UNEXPECTED ERROR: " + e.getMessage());
		}
	}
}
