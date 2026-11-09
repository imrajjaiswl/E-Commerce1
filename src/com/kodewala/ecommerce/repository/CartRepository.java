package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodewala.ecommerce.model.CartItem;

public class CartRepository {

	private Map<Integer, List<CartItem>> carts = new HashMap<>();

	// Add item to customer's cart
	public void addItemToCart(int customerId, CartItem cartItem) {

		List<CartItem> cartItems = carts.get(customerId);

		if (cartItems == null) {
			cartItems = new ArrayList<>();
			carts.put(customerId, cartItems);
		}

		cartItems.add(cartItem);
	}

	// Get customer's cart
	public List<CartItem> getCartItems(int customerId) {

		List<CartItem> cartItems = carts.get(customerId);

		if (cartItems == null) {
			return new ArrayList<>();
		}

		return new ArrayList<>(cartItems);
	}

	// Remove item from customer's cart
	public boolean removeItemFromCart(int customerId, int productId) {

		List<CartItem> cartItems = carts.get(customerId);

		if (cartItems == null) {
			return false;
		}

		return cartItems.removeIf(item -> item.getProductId() == productId);
	}

	// Clear customer's cart
	public void clearCart(int customerId) {
		carts.remove(customerId);
	}
}