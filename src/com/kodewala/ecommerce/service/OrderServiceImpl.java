package com.kodewala.ecommerce.service;

import java.util.Date;
import java.util.List;

import com.kodewala.ecommerce.exception.InsufficientStockException;
import com.kodewala.ecommerce.exception.InvalidOrderException;
import com.kodewala.ecommerce.model.CartItem;
import com.kodewala.ecommerce.model.Order;
import com.kodewala.ecommerce.model.OrderStatus;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.OrderRepository;

public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	private CartService cartService;
	private ProductService productService;
	private CustomerService customerService;

	private static int orderSequence = 1000;

	public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, ProductService productService,
			CustomerService customerService) {

		this.orderRepository = orderRepository;
		this.cartService = cartService;
		this.productService = productService;
		this.customerService = customerService;
	}

	// Place Order
	@Override
	public Order placeOrder(int customerId) {

		// 1. Validate customer
		customerService.getCustomerById(customerId);

		// 2. Get customer's cart
		List<CartItem> cartItems = cartService.getCartItems(customerId);

		if (cartItems.isEmpty()) {
			throw new InvalidOrderException("Cannot place order. Cart is empty.");
		}

		// 3. Validate stock for all products
		for (CartItem item : cartItems) {

			Product product = productService.getProductById(item.getProductId());

			if (item.getQuantity() > product.getQuantity()) {
				throw new InsufficientStockException("Insufficient stock for product: " + product.getProductName());
			}
		}

		// 4. Calculate total amount
		double totalAmount = cartItems.stream().mapToDouble(item -> item.getTotalPrice()).sum();

		// 5. Generate Order ID
		int orderId = ++orderSequence;

		// 6. Create Order object
		Order order = new Order(orderId, customerId, cartItems, totalAmount, OrderStatus.PLACED, new Date());

		// 7. Reduce product inventory
		for (CartItem item : cartItems) {

			Product product = productService.getProductById(item.getProductId());

			int remainingQuantity = product.getQuantity() - item.getQuantity();

			productService.updateProductQuantity(item.getProductId(), remainingQuantity);
		}

		// 8. Save order
		orderRepository.saveOrder(order);

		// 9. Clear customer's cart
		cartService.clearCart(customerId);

		System.out.println("Order placed successfully.");
		System.out.println("Generated Order ID: " + orderId);

		return order;
	}

	// Get Order By ID
	@Override
	public Order getOrderById(int orderId) {

		Order order = orderRepository.findOrderById(orderId);

		if (order == null) {
			throw new InvalidOrderException("Order not found with ID: " + orderId);
		}

		return order;
	}

	// Get Customer Order History
	@Override
	public List<Order> getMyOrders(int customerId) {

		customerService.getCustomerById(customerId);

		return orderRepository.getOrdersByCustomerId(customerId);
	}

	// Get All Orders (Admin)
	@Override
	public List<Order> getAllOrders() {

		return orderRepository.getAllOrders();
	}

	// Cancel Order
	@Override
	public void cancelOrder(int customerId, int orderId) {

		// 1. Validate customer
		customerService.getCustomerById(customerId);

		// 2. Find order
		Order order = getOrderById(orderId);

		// 3. Validate order ownership
		if (order.getCustomerId() != customerId) {
			throw new InvalidOrderException("You cannot cancel another customer's order.");
		}

		// 4. Validate order status
		if (order.getOrderStatus() == OrderStatus.CANCELLED) {
			throw new InvalidOrderException("Order is already cancelled.");
		}

		if (order.getOrderStatus() != OrderStatus.PLACED && order.getOrderStatus() != OrderStatus.CONFIRMED) {

			throw new InvalidOrderException("Order cannot be cancelled at this stage.");
		}

		// 5. Restore product inventory
		for (CartItem item : order.getCartItems()) {

			Product product = productService.getProductById(item.getProductId());

			int restoredQuantity = product.getQuantity() + item.getQuantity();

			productService.updateProductQuantity(item.getProductId(), restoredQuantity);
		}

		// 6. Update order status
		order.setOrderStatus(OrderStatus.CANCELLED);

		System.out.println("Order cancelled successfully.");
		System.out.println("Inventory restored successfully.");
	}

	@Override
	public void updateOrderStatus(int orderId, OrderStatus newStatus) {

		// 1. Find order
		Order order = getOrderById(orderId);

		// 2. Validate new status
		if (newStatus == null) {
			throw new InvalidOrderException("Order status cannot be null.");
		}

		OrderStatus currentStatus = order.getOrderStatus();

		// 3. Validate current status
		if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.DELIVERED) {

			throw new InvalidOrderException("Order status cannot be changed from " + currentStatus);
		}

		// 4. Validate status progression
		boolean validTransition = false;

		if (currentStatus == OrderStatus.PLACED && newStatus == OrderStatus.CONFIRMED) {

			validTransition = true;

		} else if (currentStatus == OrderStatus.CONFIRMED && newStatus == OrderStatus.SHIPPED) {

			validTransition = true;

		} else if (currentStatus == OrderStatus.SHIPPED && newStatus == OrderStatus.DELIVERED) {

			validTransition = true;
		}

		if (!validTransition) {
			throw new InvalidOrderException("Invalid order status transition: " + currentStatus + " to " + newStatus);
		}

		// 5. Update order status
		order.setOrderStatus(newStatus);

		System.out.println("Order status updated successfully.");
	}
}