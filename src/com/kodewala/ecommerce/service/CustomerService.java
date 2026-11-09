
package com.kodewala.ecommerce.service;

import java.util.List;

import com.kodewala.ecommerce.model.Customer;

public interface CustomerService {

    // Register customer
    void registerCustomer(Customer customer);

    // Search customer by ID
    Customer getCustomerById(int customerId);

    // View all customers
    List<Customer> getAllCustomers();

    // Update customer address
    void updateCustomerAddress(int customerId, String newAddress);
}
