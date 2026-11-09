package com.kodewala.ecommerce.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {

	private int orderId;
	private int customerId;
	private List<CartItem> cartItems;
	private double totalAmount;
	private OrderStatus orderStatus;
	private Date orderDate;

	public Order() {
		this.cartItems = new ArrayList<>();
	}

	public Order(int orderId, int customerId, List<CartItem> cartItems, double totalAmount, OrderStatus orderStatus,
			Date orderDate) {

		this.orderId = orderId;
		this.customerId = customerId;
		this.cartItems = new ArrayList<>(cartItems);
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public List<CartItem> getCartItems() {
		return new ArrayList<>(cartItems);
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = new ArrayList<>(cartItems);
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getOrderDate() {
		return new Date(orderDate.getTime());
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = new Date(orderDate.getTime());
	}

	@Override
	public String toString() {

		return "Order [orderId=" + orderId + ", customerId=" + customerId + ", cartItems=" + cartItems
				+ ", totalAmount=" + totalAmount + ", orderStatus=" + orderStatus + ", orderDate=" + orderDate + "]";
	}
}