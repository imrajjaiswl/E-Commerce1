# E-Commerce Product & Order Management System

A console-based e-commerce application built with **Core Java 8**,
demonstrating object-oriented programming, layered architecture, Java
Collections Framework, Stream API, lambda expressions, and custom
exception handling.

## Table of Contents

-   [Overview](#overview)
-   [Features](#features)
-   [Technology Stack](#technology-stack)
-   [Architecture](#architecture)
-   [Project Structure](#project-structure)
-   [Business Rules](#business-rules)
-   [Order Lifecycle](#order-lifecycle)
-   [Getting Started](#getting-started)
-   [Limitations](#limitations)
-   [Future Enhancements](#future-enhancements)
-   [Author](#author)

## Overview

This project simulates core online-shopping workflows using Core Java.
It separates console interaction, business logic, data access, domain
models, and custom exceptions into distinct packages.

**Admin capabilities** - Add, view, search, update, and delete
products. - View registered customers and all orders. - Manage order
status through valid lifecycle transitions.

**Customer capabilities** - Register and log in using a Customer ID. -
Browse and search products. - Manage a personal shopping cart. - Place
orders, view order history, and cancel eligible orders.

The application uses in-memory Java collections. It does not currently
use a database or external application framework.

## Features

### Product Management

-   Product CRUD operations.
-   Search by product ID, name, category, brand, and price range.
-   Update product price and available quantity.
-   Sort products by price in ascending or descending order.
-   Validate product IDs, prices, and quantities.

### Customer Management

-   Customer registration and lookup.
-   Validate required fields, email format, and 10-digit mobile numbers.
-   Prevent duplicate customer IDs.
-   Update customer address.

### Shopping Cart

-   Maintain a separate cart for each customer.
-   Add, view, increase, decrease, and remove cart items.
-   Update an existing cart item rather than creating a duplicate entry.
-   Calculate cart totals using the Stream API.
-   Validate requested quantities against available stock.

### Order Management

-   Generate order IDs during the current application session.
-   Calculate order totals from cart items.
-   Deduct inventory after successful order placement.
-   Clear the cart after order placement.
-   View customer-specific order history.
-   Cancel eligible orders and restore inventory.
-   Validate order ownership and status transitions.

## Technology Stack

  Technology                   Purpose
  ---------------------------- -----------------------------------------------------
  Java 8                       Application development
  OOP                          Domain modelling and separation of responsibilities
  Java Collections Framework   In-memory data storage
  ArrayList                    Product, order, and cart-item collections
  HashMap                      Customer and customer-cart mapping
  Stream API                   Searching, filtering, sorting, and calculations
  Lambda expressions           Functional operations
  Custom exceptions            Business validation and error reporting
  Eclipse IDE                  Development environment

**Application type:** Console application\
**Architecture:** Layered architecture\
**Persistence:** In-memory collections

## Architecture

``` text
             EcommerceApplication
              (Presentation Layer)
                       |
                       v
                  Service Layer
                (Business Logic)
                       |
                       v
                Repository Layer
                  (Data Access)
                       |
                       v
                Java Collections
                 (In-Memory Data)
```

-   **Presentation layer:** displays menus, accepts input, invokes
    services, and displays results.
-   **Service layer:** implements business rules and coordinates
    product, customer, cart, inventory, and order operations.
-   **Repository layer:** stores and retrieves data using Java
    collections.
-   **Model layer:** represents Product, Customer, CartItem, Order, and
    OrderStatus.
-   **Exception layer:** defines application-specific unchecked
    exceptions.

## Project Structure

``` text
com.kodewala.ecommerce
|
+-- main
|   +-- EcommerceApplication.java
|
+-- model
|   +-- Product.java
|   +-- Customer.java
|   +-- CartItem.java
|   +-- Order.java
|   +-- OrderStatus.java
|
+-- repository
|   +-- ProductRepository.java
|   +-- CustomerRepository.java
|   +-- CartRepository.java
|   +-- OrderRepository.java
|
+-- service
|   +-- ProductService.java
|   +-- ProductServiceImpl.java
|   +-- CustomerService.java
|   +-- CustomerServiceImpl.java
|   +-- CartService.java
|   +-- CartServiceImpl.java
|   +-- OrderService.java
|   +-- OrderServiceImpl.java
|
+-- exception
    +-- ProductNotFoundException.java
    +-- CustomerNotFoundException.java
    +-- InsufficientStockException.java
    +-- InvalidQuantityException.java
    +-- InvalidOrderException.java
    +-- CustomerAlreadyExistsException.java
```

## Business Rules

  Scenario                                     Expected behaviour
  -------------------------------------------- ----------------------------------
  Duplicate product ID                         Reject product creation
  Product not found                            `ProductNotFoundException`
  Duplicate customer ID                        `CustomerAlreadyExistsException`
  Customer not found                           `CustomerNotFoundException`
  Invalid quantity                             `InvalidQuantityException`
  Insufficient inventory                       `InsufficientStockException`
  Empty cart at checkout                       Reject order placement
  Invalid order operation                      `InvalidOrderException`
  Attempt to cancel another customer's order   Reject cancellation
  Repeated cancellation                        Reject cancellation
  Invalid status transition                    Reject status update

### Inventory example

When a product has 20 units and an order requests 3 units:

``` text
Initial inventory:     20
Ordered quantity:       3
Remaining inventory:  17
```

When an eligible order is cancelled, the ordered quantity is added back
to inventory.

## Order Lifecycle

Supported status progression:

``` text
PLACED -> CONFIRMED -> SHIPPED -> DELIVERED
```

Cancellation is permitted from `PLACED` and `CONFIRMED`:

``` text
PLACED    -> CANCELLED
CONFIRMED -> CANCELLED
```

`DELIVERED` and `CANCELLED` are terminal states. Invalid transitions are
rejected by the service layer.

## Java Concepts Demonstrated

-   Encapsulation through private fields and accessors.
-   Abstraction through service interfaces.
-   Inheritance through custom exceptions extending `RuntimeException`.
-   Polymorphism through interface-based service implementations.
-   Constructor-based dependency injection.
-   `List`, `ArrayList`, `Map`, and `HashMap`.
-   Lambda expressions and Stream API operations such as `filter()`,
    `sorted()`, `mapToDouble()`, and `sum()`.
-   Enum-based order status management.
-   Separation of concerns and business validation.

## Getting Started

### Prerequisites

-   JDK 8 or a compatible later JDK.
-   Eclipse IDE or another Java IDE.
-   Git.

### 1. Clone the repository

Update the URL if your GitHub repository has a different name.

``` bash
git clone https://github.com/imrajjaiswl/E-Commerce1.git
```

### 2. Import into Eclipse

1.  Open Eclipse.
2.  Select **File → Import**.
3.  Choose **General → Existing Projects into Workspace**.
4.  Select the cloned project directory.
5.  Finish the import.

### 3. Configure the JDK

Configure the project build path to use Java 8 or a compatible JDK.

### 4. Run the application

Open and run:

``` text
com.kodewala.ecommerce.main.EcommerceApplication
```

The main menu will appear in the console.

## Sample Workflow

``` text
========== E-COMMERCE APPLICATION ==========

1. Admin
2. Customer
3. Exit
```

Example customer dashboard:

``` text
========== CUSTOMER DASHBOARD ==========

1. View Products
2. Search Products
3. Add Product to Cart
4. View / Manage Cart
5. Remove Product from Cart
6. Place Order
7. View My Orders
8. Cancel Order
9. Logout
```

Example order confirmation:

``` text
========== ORDER CONFIRMATION ==========

Order ID     : 1003
Customer ID  : 101
Total Amount : 16000.0
Order Status : PLACED

Product Name : Laptop
Price        : 8000.0
Quantity     : 2
Total Price  : 16000.0
```

## Custom Exceptions

  Exception                          Responsibility
  ---------------------------------- ------------------------------------------
  `ProductNotFoundException`         Product does not exist
  `CustomerNotFoundException`        Customer does not exist
  `InsufficientStockException`       Requested inventory is unavailable
  `InvalidQuantityException`         Quantity is invalid
  `InvalidOrderException`            Order operation or transition is invalid
  `CustomerAlreadyExistsException`   Customer ID is already registered

## Limitations

-   Data is stored in memory and is lost when the application
    terminates.
-   Customer login uses Customer ID; password-based authentication is
    not implemented.
-   Admin authentication is not implemented.
-   Payment gateway integration is not included.
-   Database persistence is not included.
-   This is a single-process console application.
-   Inventory changes are not protected by database transactions or
    concurrent transaction controls.

## Future Enhancements

-   MySQL persistence using JDBC.
-   REST APIs using Spring Boot.
-   Authentication and authorization using Spring Security and JWT.
-   Role-based access control for Admin and Customer.
-   Payment gateway integration.
-   Transaction management for reliable inventory updates.
-   Logging using SLF4J and Logback.
-   Automated unit and integration testing using JUnit and Mockito.
-   Web-based user interface.

## Author

**Rajkumar Jaiswal**

Java Backend Developer | Java Full Stack Developer

-   **GitHub:** https://github.com/imrajjaiswl
-   **LinkedIn:** https://www.linkedin.com/in/imrajjaiswl/

------------------------------------------------------------------------

*Built with Core Java to demonstrate practical e-commerce workflows,
layered design, collections, Stream API, and custom exception handling.*
