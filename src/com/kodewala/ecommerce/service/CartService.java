package com.kodewala.ecommerce.service;

import java.util.List;

import com.kodewala.ecommerce.model.CartItem;

public interface CartService {

    // Add product to customer's cart
    void addItemToCart(int customerId, int productId, int quantity);

    // View customer's cart
    List<CartItem> getCartItems(int customerId);

    // Remove product from customer's cart
    void removeItemFromCart(int customerId, int productId);

    // Increase product quantity
    void increaseQuantity(int customerId, int productId, int quantity);

    // Decrease product quantity
    void decreaseQuantity(int customerId, int productId, int quantity);

    // Calculate cart total
    double calculateCartTotal(int customerId);

    // Clear customer's cart
    void clearCart(int customerId);
}