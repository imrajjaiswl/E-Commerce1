package com.kodewala.ecommerce.model;

public class Customer {

	// 1. Instance variables
	private int customerId;
	private String name;
	private String email;
	private String mobile;
	private String address;

	// 3. Parameterized constructor
	public Customer(int customerId, String name, String email, String mobile, String address) {

		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.address = address;
	}

	// 4. Getters and Setters

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// 5. toString() method
	@Override
	public String toString() {

		return "Customer [customerId=" + customerId + ", name=" + name + ", email=" + email + ", mobile=" + mobile
				+ ", address=" + address + "]";
	}
}
