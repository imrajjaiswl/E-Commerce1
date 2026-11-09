
package com.kodewala.ecommerce.service;

import java.util.List;

import com.kodewala.ecommerce.exception.CustomerAlreadyExistsException;
import com.kodewala.ecommerce.exception.CustomerNotFoundException;
import com.kodewala.ecommerce.model.Customer;
import com.kodewala.ecommerce.repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	// Register customer
	@Override
	public void registerCustomer(Customer customer) {

		if (customer == null) {
			throw new IllegalArgumentException("Customer cannot be null");
		}

		if (customer.getCustomerId() <= 0) {
			throw new IllegalArgumentException("Customer ID must be greater than zero");
		}

		if (customer.getName() == null || customer.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Customer name cannot be empty");
		}

		if (customer.getEmail() == null || !customer.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			throw new IllegalArgumentException("Please enter a valid email address");
		}

		if (customer.getMobile() == null || !customer.getMobile().matches("[0-9]{10}")) {
			throw new IllegalArgumentException("Mobile number must contain exactly 10 digits");
		}

		if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
			throw new IllegalArgumentException("Customer address cannot be empty");
		}

		if (customerRepository.findCustomerById(customer.getCustomerId()) != null) {

			throw new CustomerAlreadyExistsException("Customer ID " + customer.getCustomerId() + " already exists");
		}

		customerRepository.addCustomer(customer);

		System.out.println("Customer registered successfully.");
	}

	// Search customer by ID
	@Override
	public Customer getCustomerById(int customerId) {

		Customer customer = customerRepository.findCustomerById(customerId);

		if (customer == null) {
			throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
		}

		return customer;
	}

	// Get all customers
	@Override
	public List<Customer> getAllCustomers() {

		return customerRepository.getAllCustomers();
	}

	// Update customer address
	@Override
	public void updateCustomerAddress(int customerId, String newAddress) {

		if (newAddress == null || newAddress.trim().isEmpty()) {
			throw new IllegalArgumentException("Address cannot be empty");
		}

		Customer customer = getCustomerById(customerId);

		customer.setAddress(newAddress);

		System.out.println("Customer address updated successfully.");
	}
}
