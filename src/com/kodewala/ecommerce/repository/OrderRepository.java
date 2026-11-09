package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;

import com.kodewala.ecommerce.model.Order;

public class OrderRepository {

	private List<Order> orders = new ArrayList<>();

	// Save order
	public void saveOrder(Order order) {
		orders.add(order);
	}

	// Find order by ID
	public Order findOrderById(int orderId) {

		for (Order order : orders) {

			if (order.getOrderId() == orderId) {
				return order;
			}
		}

		return null;
	}

	// Get all orders
	public List<Order> getAllOrders() {
		return new ArrayList<>(orders);
	}

	// Get orders by customer ID
	public List<Order> getOrdersByCustomerId(int customerId) {

		List<Order> customerOrders = new ArrayList<>();

		for (Order order : orders) {

			if (order.getCustomerId() == customerId) {
				customerOrders.add(order);
			}
		}

		return customerOrders;
	}
}