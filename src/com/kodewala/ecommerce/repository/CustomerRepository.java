
package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodewala.ecommerce.model.Customer;

public class CustomerRepository {

	// Store customers using HashMap
	private Map<Integer, Customer> customers = new HashMap<>();

	// Add customer
	public void addCustomer(Customer customer) {

		customers.put(customer.getCustomerId(), customer);
	}

	// Find customer by ID
	public Customer findCustomerById(int customerId) {

		return customers.get(customerId);
	}

	// Get all customers
	public List<Customer> getAllCustomers() {

		return new ArrayList<>(customers.values());
	}

	// Update customer address
	public boolean updateCustomerAddress(int customerId, String newAddress) {

		Customer customer = customers.get(customerId);

		if (customer != null) {

			customer.setAddress(newAddress);
			return true;
		}

		return false;
	}
}
