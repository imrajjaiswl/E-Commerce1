package com.kodewala.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import com.kodewala.ecommerce.exception.InsufficientStockException;
import com.kodewala.ecommerce.exception.InvalidQuantityException;
import com.kodewala.ecommerce.model.CartItem;
import com.kodewala.ecommerce.model.Customer;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.CartRepository;

public class CartServiceImpl implements CartService {

	private CartRepository cartRepository;
	private ProductService productService;
	private CustomerService customerService;

	public CartServiceImpl(CartRepository cartRepository, ProductService productService,
			CustomerService customerService) {

		this.cartRepository = cartRepository;
		this.productService = productService;
		this.customerService = customerService;
	}

	@Override
	public void addItemToCart(int customerId, int productId, int quantity) {

		// 1. Validate customer
		Customer customer = customerService.getCustomerById(customerId);

		// 2. Validate quantity
		if (quantity <= 0) {
			throw new InvalidQuantityException("Quantity must be greater than zero.");
		}

		// 3. Retrieve product
		Product product = productService.getProductById(productId);

		// 4. Check available stock
		if (quantity > product.getQuantity()) {
			throw new InsufficientStockException("Requested quantity exceeds available stock.");
		}

		// 5. Retrieve existing cart items
		List<CartItem> cartItems = cartRepository.getCartItems(customerId);

		// 6. Check whether product already exists in cart
		CartItem existingItem = null;

		for (CartItem item : cartItems) {

			if (item.getProductId() == productId) {
				existingItem = item;
				break;
			}
		}

		// 7. If product already exists, increase quantity
		if (existingItem != null) {

			int updatedQuantity = existingItem.getQuantity() + quantity;

			if (updatedQuantity > product.getQuantity()) {
				throw new InsufficientStockException("Total cart quantity exceeds available stock.");
			}

			cartRepository.removeItemFromCart(customerId, productId);

			CartItem updatedItem = new CartItem(product.getProductId(), product.getProductName(), product.getPrice(),
					updatedQuantity);

			cartRepository.addItemToCart(customerId, updatedItem);

		} else {

			// 8. Create a new cart item
			CartItem cartItem = new CartItem(product.getProductId(), product.getProductName(), product.getPrice(),
					quantity);

			cartRepository.addItemToCart(customerId, cartItem);
		}

		System.out.println("Product added to cart successfully.");
	}

	@Override
	public List<CartItem> getCartItems(int customerId) {

		customerService.getCustomerById(customerId);

		return cartRepository.getCartItems(customerId);
	}

	@Override
	public void removeItemFromCart(int customerId, int productId) {

		customerService.getCustomerById(customerId);

		boolean removed = cartRepository.removeItemFromCart(customerId, productId);

		if (!removed) {
			throw new IllegalArgumentException("Product not found in customer's cart.");
		}

		System.out.println("Product removed from cart successfully.");
	}

	@Override
	public void increaseQuantity(int customerId, int productId, int quantity) {

		if (quantity <= 0) {
			throw new InvalidQuantityException("Quantity must be greater than zero.");
		}

		updateCartItemQuantity(customerId, productId, quantity);
	}

	@Override
	public void decreaseQuantity(int customerId, int productId, int quantity) {

		if (quantity <= 0) {
			throw new InvalidQuantityException("Quantity must be greater than zero.");
		}

		List<CartItem> cartItems = getCartItems(customerId);

		CartItem existingItem = findCartItem(cartItems, productId);

		if (existingItem == null) {
			throw new IllegalArgumentException("Product not found in customer's cart.");
		}

		int updatedQuantity = existingItem.getQuantity() - quantity;

		if (updatedQuantity <= 0) {
			throw new InvalidQuantityException(
					"Remaining quantity must be greater than zero. " + "Use remove product to remove it completely.");
		}

		cartRepository.removeItemFromCart(customerId, productId);

		CartItem updatedItem = new CartItem(existingItem.getProductId(), existingItem.getProductName(),
				existingItem.getPrice(), updatedQuantity);

		cartRepository.addItemToCart(customerId, updatedItem);
	}

	@Override
	public double calculateCartTotal(int customerId) {

		List<CartItem> cartItems = getCartItems(customerId);

		return cartItems.stream().mapToDouble(item -> item.getTotalPrice()).sum();
	}

	@Override
	public void clearCart(int customerId) {

		customerService.getCustomerById(customerId);

		cartRepository.clearCart(customerId);

		System.out.println("Cart cleared successfully.");
	}

	private void updateCartItemQuantity(int customerId, int productId, int quantity) {

		List<CartItem> cartItems = getCartItems(customerId);

		CartItem existingItem = findCartItem(cartItems, productId);

		if (existingItem == null) {
			throw new IllegalArgumentException("Product not found in customer's cart.");
		}

		Product product = productService.getProductById(productId);

		int updatedQuantity = existingItem.getQuantity() + quantity;

		if (updatedQuantity > product.getQuantity()) {
			throw new InsufficientStockException("Requested quantity exceeds available stock.");
		}

		cartRepository.removeItemFromCart(customerId, productId);

		CartItem updatedItem = new CartItem(product.getProductId(), product.getProductName(), product.getPrice(),
				updatedQuantity);

		cartRepository.addItemToCart(customerId, updatedItem);

		System.out.println("Cart quantity updated successfully.");
	}

	private CartItem findCartItem(List<CartItem> cartItems, int productId) {

		List<CartItem> matchingItems = cartItems.stream().filter(item -> item.getProductId() == productId)
				.collect(Collectors.toList());

		if (matchingItems.isEmpty()) {
			return null;
		}

		return matchingItems.get(0);
	}
}