package com.kodewala.ecommerce.service;

import java.util.List;

import com.kodewala.ecommerce.model.Order;
import com.kodewala.ecommerce.model.OrderStatus;

public interface OrderService {

	Order placeOrder(int customerId);

	Order getOrderById(int orderId);

	List<Order> getMyOrders(int customerId);

	List<Order> getAllOrders();

	void cancelOrder(int customerId, int orderId);

	void updateOrderStatus(int orderId, OrderStatus newStatus);
}