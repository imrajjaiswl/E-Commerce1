package com.kodewala.ecommerce.main;

import java.util.List;
import java.util.Scanner;

import com.kodewala.ecommerce.exception.CustomerNotFoundException;
import com.kodewala.ecommerce.model.CartItem;
import com.kodewala.ecommerce.model.Customer;
import com.kodewala.ecommerce.model.Order;
import com.kodewala.ecommerce.model.OrderStatus;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.CartRepository;
import com.kodewala.ecommerce.repository.CustomerRepository;
import com.kodewala.ecommerce.repository.OrderRepository;
import com.kodewala.ecommerce.repository.ProductRepository;
import com.kodewala.ecommerce.service.CartService;
import com.kodewala.ecommerce.service.CartServiceImpl;
import com.kodewala.ecommerce.service.CustomerService;
import com.kodewala.ecommerce.service.CustomerServiceImpl;
import com.kodewala.ecommerce.service.OrderService;
import com.kodewala.ecommerce.service.OrderServiceImpl;
import com.kodewala.ecommerce.service.ProductService;
import com.kodewala.ecommerce.service.ProductServiceImpl;

public class EcommerceApplication {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		ProductRepository productRepository = new ProductRepository();
		ProductService productService = new ProductServiceImpl(productRepository);

		CustomerRepository customerRepository = new CustomerRepository();
		CustomerService customerService = new CustomerServiceImpl(customerRepository);

		CartRepository cartRepository = new CartRepository();
		CartService cartService = new CartServiceImpl(cartRepository, productService, customerService);

		OrderRepository orderRepository = new OrderRepository();
		OrderService orderService = new OrderServiceImpl(orderRepository, cartService, productService, customerService);

		boolean running = true;

		while (running) {
			try {
				System.out.println("\n================================");
				System.out.println("       E-COMMERCE SYSTEM");
				System.out.println("================================");
				System.out.println("1. Admin");
				System.out.println("2. Customer");
				System.out.println("3. Exit");
				System.out.print("Enter your choice: ");

				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
				case 1:
					adminMenu(scanner, productService, customerService, orderService);
					break;
				case 2:
					customerMenu(scanner, customerService, productService, cartService, orderService);
					break;
				case 3:
					running = false;
					System.out.println("Thank you for using E-Commerce Application.");
					break;
				default:
					System.err.println("Invalid choice. Please select 1, 2 or 3.");
				}
			} catch (NumberFormatException e) {
				System.err.println("Invalid input. Please enter a numeric choice.");
			} catch (Exception e) {
				System.err.println("ERROR: " + e.getMessage());
			}
		}

		scanner.close();
	}

	// -------------------- ADMIN MENU --------------------

	private static void adminMenu(Scanner scanner, ProductService productService, CustomerService customerService,
			OrderService orderService) {

		boolean adminRunning = true;

		while (adminRunning) {
			try {
				System.out.println("\n========== ADMIN MENU ==========");
				System.out.println("1. Add Product");
				System.out.println("2. View Products");
				System.out.println("3. Search Product");
				System.out.println("4. Update Product");
				System.out.println("5. Delete Product");
				System.out.println("6. View Customers");
				System.out.println("7. Manage Orders");
				System.out.println("8. Exit Admin");
				System.out.print("Enter your choice: ");

				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
				case 1:
					addProduct(scanner, productService);
					break;
				case 2:
					viewProducts(productService);
					break;
				case 3:
					searchProduct(scanner, productService);
					break;
				case 4:
					updateProduct(scanner, productService);
					break;
				case 5:
					deleteProduct(scanner, productService);
					break;
				case 6:
					viewCustomers(customerService);
					break;
				case 7:
					manageOrders(scanner, orderService);
					break;
				case 8:
					adminRunning = false;
					System.out.println("Admin logged out successfully.");
					break;
				default:
					System.err.println("Invalid admin menu choice.");
				}
			} catch (NumberFormatException e) {
				System.err.println("Please enter a valid numeric value.");
			} catch (Exception e) {
				System.err.println("ADMIN ERROR: " + e.getMessage());
			}
		}
	}

	private static void addProduct(Scanner scanner, ProductService service) {

		System.out.println("\n========== ADD PRODUCT ==========");

		System.out.print("Enter Product ID: ");
		int productId = Integer.parseInt(scanner.nextLine());

		System.out.print("Enter Product Name: ");
		String productName = scanner.nextLine();

		System.out.print("Enter Category: ");
		String category = scanner.nextLine();

		System.out.print("Enter Price: ");
		double price = Double.parseDouble(scanner.nextLine());

		System.out.print("Enter Quantity: ");
		int quantity = Integer.parseInt(scanner.nextLine());

		System.out.print("Enter Brand: ");
		String brand = scanner.nextLine();

		Product product = new Product(productId, productName, category, price, quantity, brand);
		service.addProduct(product);

		System.out.println("Product added successfully.");
	}

	private static void viewProducts(ProductService service) {

		System.out.println("\n========== ALL PRODUCTS ==========");
		List<Product> products = service.getAllProducts();

		if (products.isEmpty()) {
			System.out.println("No products available.");
			return;
		}

		for (Product product : products) {
			System.out.println(product);
		}
	}

	private static void searchProduct(Scanner scanner, ProductService service) {

		System.out.println("\n========== SEARCH PRODUCT ==========");
		System.out.println("1. Search By ID");
		System.out.println("2. Search By Name");
		System.out.println("3. Search By Category");
		System.out.println("4. Search By Brand");
		System.out.println("5. Search By Price Range");
		System.out.print("Enter your choice: ");

		int choice = Integer.parseInt(scanner.nextLine());

		switch (choice) {
		case 1:
			System.out.print("Enter Product ID: ");
			int id = Integer.parseInt(scanner.nextLine());
			System.out.println(service.getProductById(id));
			break;
		case 2:
			System.out.print("Enter Product Name: ");
			displayProducts(service.searchProductsByName(scanner.nextLine()));
			break;
		case 3:
			System.out.print("Enter Category: ");
			displayProducts(service.searchProductsByCategory(scanner.nextLine()));
			break;
		case 4:
			System.out.print("Enter Brand: ");
			displayProducts(service.searchProductsByBrand(scanner.nextLine()));
			break;
		case 5:
			System.out.print("Enter Minimum Price: ");
			double minPrice = Double.parseDouble(scanner.nextLine());
			System.out.print("Enter Maximum Price: ");
			double maxPrice = Double.parseDouble(scanner.nextLine());

			if (minPrice > maxPrice) {
				System.err.println("Minimum price cannot be greater than maximum price.");
				return;
			}

			displayProducts(service.searchProductsByPriceRange(minPrice, maxPrice));
			break;
		default:
			System.err.println("Invalid search choice.");
		}
	}

	private static void displayProducts(List<Product> products) {

		if (products == null || products.isEmpty()) {
			System.out.println("No matching products found.");
			return;
		}

		for (Product product : products) {
			System.out.println(product);
		}
	}

	private static void updateProduct(Scanner scanner, ProductService service) {

		System.out.println("\n========== UPDATE PRODUCT ==========");
		System.out.println("1. Update Price");
		System.out.println("2. Update Quantity");
		System.out.print("Enter your choice: ");

		int choice = Integer.parseInt(scanner.nextLine());

		System.out.print("Enter Product ID: ");
		int productId = Integer.parseInt(scanner.nextLine());

		switch (choice) {
		case 1:
			System.out.print("Enter New Price: ");
			double price = Double.parseDouble(scanner.nextLine());
			service.updateProductPrice(productId, price);
			break;
		case 2:
			System.out.print("Enter New Quantity: ");
			int quantity = Integer.parseInt(scanner.nextLine());
			service.updateProductQuantity(productId, quantity);
			break;
		default:
			System.err.println("Invalid update choice.");
		}
	}

	private static void deleteProduct(Scanner scanner, ProductService service) {

		System.out.print("Enter Product ID to delete: ");
		int productId = Integer.parseInt(scanner.nextLine());

		service.deleteProduct(productId);
		System.out.println("Product deleted successfully.");
	}

	private static void viewCustomers(CustomerService service) {

		System.out.println("\n========== ALL CUSTOMERS ==========");
		List<Customer> customers = service.getAllCustomers();

		if (customers.isEmpty()) {
			System.out.println("No customers registered.");
			return;
		}

		for (Customer customer : customers) {
			System.out.println(customer);
		}
	}

	// -------------------- CUSTOMER MENU --------------------

	private static void customerMenu(Scanner scanner, CustomerService customerService, ProductService productService,
			CartService cartService, OrderService orderService) {

		Customer loggedInCustomer = null;
		boolean customerRunning = true;

		while (customerRunning) {

			try {

				// GUEST MENU
				if (loggedInCustomer == null) {

					System.out.println("\n========== GUEST MENU ==========");
					System.out.println("1. Register");
					System.out.println("2. Login");
					System.out.println("3. Back to Main Menu");
					System.out.print("Enter your choice: ");

					int choice = Integer.parseInt(scanner.nextLine());

					switch (choice) {

					case 1:
						registerCustomer(scanner, customerService);
						break;

					case 2:
						loggedInCustomer = loginCustomer(scanner, customerService);
						break;

					case 3:
						customerRunning = false;
						break;

					default:
						System.err.println("Invalid choice.");
					}

				} else {

					// LOGGED-IN CUSTOMER DASHBOARD
					System.out.println("\n========== CUSTOMER DASHBOARD ==========");
					System.out.println("Welcome, " + loggedInCustomer.getName());

					System.out.println("1. View Products");
					System.out.println("2. Search Products");
					System.out.println("3. Add Product to Cart");
					System.out.println("4. View / Manage Cart");
					System.out.println("5. Remove Product from Cart");
					System.out.println("6. Place Order");
					System.out.println("7. View My Orders");
					System.out.println("8. Cancel Order");
					System.out.println("9. Logout");

					System.out.print("Enter your choice: ");

					int choice = Integer.parseInt(scanner.nextLine());

					switch (choice) {

					case 1:
						viewProducts(productService);
						break;

					case 2:
						searchProduct(scanner, productService);
						break;

					case 3:
						addProductToCart(scanner, cartService, loggedInCustomer);
						break;

					case 4:
						manageCart(scanner, cartService, loggedInCustomer);
						break;

					case 5:
						removeProductFromCart(scanner, cartService, loggedInCustomer);
						break;

					case 6:
						placeCustomerOrder(orderService, loggedInCustomer);
						break;

					case 7:
						viewMyOrders(orderService, loggedInCustomer);
						break;

					case 8:
						cancelCustomerOrder(scanner, orderService, loggedInCustomer);
						break;

					case 9:
						loggedInCustomer = null;
						System.out.println("Logout successful.");
						break;

					default:
						System.err.println("Invalid customer choice.");
					}
				}

			} catch (NumberFormatException e) {

				System.err.println("Please enter a valid numeric choice.");

			} catch (Exception e) {

				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	private static boolean requireLogin(Customer customer) {

		if (customer == null) {
			System.err.println("Please login first.");
			return false;
		}

		return true;
	}

	private static void registerCustomer(Scanner scanner, CustomerService customerService) {

		try {
			System.out.println("\n========== CUSTOMER REGISTRATION ==========");

			System.out.print("Enter Customer ID: ");
			int customerId = Integer.parseInt(scanner.nextLine());

			System.out.print("Enter Name: ");
			String name = scanner.nextLine();

			System.out.print("Enter Email: ");
			String email = scanner.nextLine();

			System.out.print("Enter Mobile: ");
			String mobile = scanner.nextLine();

			System.out.print("Enter Address: ");
			String address = scanner.nextLine();

			Customer customer = new Customer(customerId, name, email, mobile, address);
			customerService.registerCustomer(customer);

			System.out.println("Customer registration completed successfully.");
		} catch (NumberFormatException e) {
			System.err.println("Customer ID must be numeric.");
		} catch (Exception e) {
			System.err.println("Registration failed: " + e.getMessage());
		}
	}

	private static Customer loginCustomer(Scanner scanner, CustomerService customerService) {

		try {
			System.out.println("\n========== CUSTOMER LOGIN ==========");
			System.out.print("Enter Customer ID: ");

			int customerId = Integer.parseInt(scanner.nextLine());
			Customer customer = customerService.getCustomerById(customerId);

			System.out.println("Login successful!");
			System.out.println("Welcome, " + customer.getName());

			return customer;
		} catch (NumberFormatException e) {
			System.err.println("Customer ID must be numeric.");
		} catch (CustomerNotFoundException e) {
			System.err.println("Login failed: " + e.getMessage());
		}

		return null;
	}

	// -------------------- CART OPERATIONS --------------------

	private static void addProductToCart(Scanner scanner, CartService cartService, Customer loggedInCustomer) {

		try {
			System.out.print("Enter Product ID: ");
			int productId = Integer.parseInt(scanner.nextLine());

			System.out.print("Enter Quantity: ");
			int quantity = Integer.parseInt(scanner.nextLine());

			cartService.addItemToCart(loggedInCustomer.getCustomerId(), productId, quantity);
			System.out.println("Product added to cart successfully.");
		} catch (NumberFormatException e) {
			System.err.println("Product ID and quantity must be numeric.");
		} catch (Exception e) {
			System.err.println("Unable to add product to cart: " + e.getMessage());
		}
	}

	private static void manageCart(Scanner scanner, CartService cartService, Customer loggedInCustomer) {

		boolean running = true;

		while (running) {
			System.out.println("\n========== CART MANAGEMENT ==========");
			System.out.println("1. View Cart");
			System.out.println("2. Increase Product Quantity");
			System.out.println("3. Decrease Product Quantity");
			System.out.println("4. Back to Customer Menu");
			System.out.print("Enter your choice: ");

			try {
				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
				case 1:
					viewCart(cartService, loggedInCustomer);
					break;
				case 2:
					increaseCartQuantity(scanner, cartService, loggedInCustomer);
					break;
				case 3:
					decreaseCartQuantity(scanner, cartService, loggedInCustomer);
					break;
				case 4:
					running = false;
					break;
				default:
					System.err.println("Invalid choice.");
				}
			} catch (NumberFormatException e) {
				System.err.println("Please enter a valid numeric choice.");
			}
		}
	}

	private static void viewCart(CartService cartService, Customer loggedInCustomer) {

		try {
			List<CartItem> cartItems = cartService.getCartItems(loggedInCustomer.getCustomerId());

			if (cartItems == null || cartItems.isEmpty()) {
				System.out.println("Your cart is empty.");
				return;
			}

			double total = 0.0;

			System.out.println("\n========== YOUR CART ==========");
			for (CartItem item : cartItems) {
				System.out.println("--------------------------------");
				System.out.println("Product ID   : " + item.getProductId());
				System.out.println("Product Name : " + item.getProductName());
				System.out.println("Price        : " + item.getPrice());
				System.out.println("Quantity     : " + item.getQuantity());
				System.out.println("Total Price  : " + item.getTotalPrice());
				total += item.getTotalPrice();
			}

			System.out.println("--------------------------------");
			System.out.println("Cart Total   : " + total);
		} catch (Exception e) {
			System.err.println("Unable to display cart: " + e.getMessage());
		}
	}

	private static void removeProductFromCart(Scanner scanner, CartService cartService, Customer loggedInCustomer) {

		try {
			System.out.print("Enter Product ID to remove: ");
			int productId = Integer.parseInt(scanner.nextLine());

			cartService.removeItemFromCart(loggedInCustomer.getCustomerId(), productId);
			System.out.println("Product removed from cart successfully.");
		} catch (NumberFormatException e) {
			System.err.println("Product ID must be numeric.");
		} catch (Exception e) {
			System.err.println("Unable to remove product from cart: " + e.getMessage());
		}
	}

	private static void increaseCartQuantity(Scanner scanner, CartService cartService, Customer loggedInCustomer) {

		try {
			System.out.print("Enter Product ID: ");
			int productId = Integer.parseInt(scanner.nextLine());

			System.out.print("Enter quantity to increase: ");
			int quantity = Integer.parseInt(scanner.nextLine());

			cartService.increaseQuantity(loggedInCustomer.getCustomerId(), productId, quantity);
			System.out.println("Cart quantity increased successfully.");
		} catch (NumberFormatException e) {
			System.err.println("Please enter valid numeric values.");
		} catch (Exception e) {
			System.err.println("Unable to increase quantity: " + e.getMessage());
		}
	}

	private static void decreaseCartQuantity(Scanner scanner, CartService cartService, Customer loggedInCustomer) {

		try {
			System.out.print("Enter Product ID: ");
			int productId = Integer.parseInt(scanner.nextLine());

			System.out.print("Enter quantity to decrease: ");
			int quantity = Integer.parseInt(scanner.nextLine());

			cartService.decreaseQuantity(loggedInCustomer.getCustomerId(), productId, quantity);
			System.out.println("Cart quantity decreased successfully.");
		} catch (NumberFormatException e) {
			System.err.println("Please enter valid numeric values.");
		} catch (Exception e) {
			System.err.println("Unable to decrease quantity: " + e.getMessage());
		}
	}

	// -------------------- ORDER OPERATIONS --------------------

	private static void placeCustomerOrder(OrderService orderService, Customer loggedInCustomer) {

		try {
			Order order = orderService.placeOrder(loggedInCustomer.getCustomerId());

			System.out.println("\n========== ORDER CONFIRMATION ==========");
			System.out.println("Order ID     : " + order.getOrderId());
			System.out.println("Customer ID  : " + order.getCustomerId());
			System.out.println("Total Amount : " + order.getTotalAmount());
			System.out.println("Order Status : " + order.getOrderStatus());
			System.out.println("Order Date   : " + order.getOrderDate());

			System.out.println("\nOrdered Products:");
			for (CartItem item : order.getCartItems()) {
				System.out.println("--------------------------------");
				System.out.println("Product Name : " + item.getProductName());
				System.out.println("Price        : " + item.getPrice());
				System.out.println("Quantity     : " + item.getQuantity());
				System.out.println("Total Price  : " + item.getTotalPrice());
			}
		} catch (Exception e) {
			System.err.println("Order failed: " + e.getMessage());
		}
	}

	private static void viewMyOrders(OrderService orderService, Customer loggedInCustomer) {

		try {
			List<Order> orders = orderService.getMyOrders(loggedInCustomer.getCustomerId());

			if (orders == null || orders.isEmpty()) {
				System.out.println("No orders found.");
				return;
			}

			System.out.println("\n========== MY ORDER HISTORY ==========");
			for (Order order : orders) {
				System.out.println("--------------------------------");
				System.out.println("Order ID     : " + order.getOrderId());
				System.out.println("Order Date   : " + order.getOrderDate());
				System.out.println("Total Amount : " + order.getTotalAmount());
				System.out.println("Order Status : " + order.getOrderStatus());
			}
		} catch (Exception e) {
			System.err.println("Unable to retrieve orders: " + e.getMessage());
		}
	}

	private static void cancelCustomerOrder(Scanner scanner, OrderService orderService, Customer loggedInCustomer) {

		try {
			System.out.print("Enter Order ID to cancel: ");
			int orderId = Integer.parseInt(scanner.nextLine());

			orderService.cancelOrder(loggedInCustomer.getCustomerId(), orderId);
			System.out.println("Order cancellation request completed.");
		} catch (NumberFormatException e) {
			System.err.println("Please enter a valid Order ID.");
		} catch (Exception e) {
			System.err.println("Cancellation failed: " + e.getMessage());
		}
	}

	// -------------------- ADMIN ORDER MANAGEMENT --------------------

	private static void viewAllOrders(OrderService orderService) {

		try {
			List<Order> orders = orderService.getAllOrders();

			if (orders == null || orders.isEmpty()) {
				System.out.println("No orders available.");
				return;
			}

			System.out.println("\n========== ALL ORDERS ==========");
			for (Order order : orders) {
				System.out.println("--------------------------------");
				System.out.println("Order ID     : " + order.getOrderId());
				System.out.println("Customer ID  : " + order.getCustomerId());
				System.out.println("Total Amount : " + order.getTotalAmount());
				System.out.println("Order Status : " + order.getOrderStatus());
				System.out.println("Order Date   : " + order.getOrderDate());
			}
		} catch (Exception e) {
			System.err.println("Unable to retrieve orders: " + e.getMessage());
		}
	}

	private static void manageOrders(Scanner scanner, OrderService orderService) {

		boolean running = true;

		while (running) {
			System.out.println("\n========== ORDER MANAGEMENT ==========");
			System.out.println("1. View All Orders");
			System.out.println("2. Update Order Status");
			System.out.println("3. Back to Admin Menu");
			System.out.print("Enter your choice: ");

			try {
				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
				case 1:
					viewAllOrders(orderService);
					break;
				case 2:
					updateOrderStatus(scanner, orderService);
					break;
				case 3:
					running = false;
					break;
				default:
					System.err.println("Invalid choice.");
				}
			} catch (NumberFormatException e) {
				System.err.println("Please enter a valid numeric choice.");
			}
		}
	}

	private static void updateOrderStatus(Scanner scanner, OrderService orderService) {

		try {
			System.out.print("Enter Order ID: ");
			int orderId = Integer.parseInt(scanner.nextLine());

			System.out.println("\nSelect New Order Status:");
			System.out.println("1. CONFIRMED");
			System.out.println("2. SHIPPED");
			System.out.println("3. DELIVERED");
			System.out.print("Enter your choice: ");

			int choice = Integer.parseInt(scanner.nextLine());
			OrderStatus newStatus;

			switch (choice) {
			case 1:
				newStatus = OrderStatus.CONFIRMED;
				break;
			case 2:
				newStatus = OrderStatus.SHIPPED;
				break;
			case 3:
				newStatus = OrderStatus.DELIVERED;
				break;
			default:
				System.err.println("Invalid order status choice.");
				return;
			}

			orderService.updateOrderStatus(orderId, newStatus);
			System.out.println("Order status updated successfully.");
		} catch (NumberFormatException e) {
			System.err.println("Please enter valid numeric values.");
		} catch (Exception e) {
			System.err.println("Unable to update order: " + e.getMessage());
		}
	}
}
