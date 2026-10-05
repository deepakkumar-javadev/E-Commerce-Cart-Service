# E-Commerce Cart Service

Cart Service is a Spring Boot microservice responsible for managing the shopping cart functionality in an e-commerce application.

It allows customers to add products to their cart, update quantities, view cart items, and remove products from the cart. The service communicates with Product, Inventory, and Order services using Feign Client.

## Features

* Add products to cart
* View customer's cart
* Update product quantity
* Remove products from cart
* Validate product details with Product Service
* Check product availability with Inventory Service
* Communicate with Order Service
* JWT-based authentication
* Internal service authentication
* REST APIs
* MySQL database

## Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Cloud OpenFeign
* Spring Security
* JWT
* MySQL
* Maven
* Eureka Service Discovery

## Microservice Communication

```text
                    ┌─────────────────┐
                    │   API Gateway   │
                    └────────┬────────┘
                             │
                             ↓
                    ┌─────────────────┐
                    │   Cart Service  │
                    │      :8085      │
                    └────┬────┬───────┘
                         │    │
              ┌──────────┘    └──────────┐
              ↓                          ↓
      ┌─────────────────┐        ┌─────────────────┐
      │ Product Service │        │Inventory Service │
      └─────────────────┘        └─────────────────┘
                         │
                         ↓
                  ┌───────────────┐
                  │ Order Service │
                  └───────────────┘
```

Cart Service uses **Feign Client** for synchronous communication with other microservices.

## Main Responsibilities

### Cart Management

* Create and manage customer carts
* Add products and quantities
* Update cart item quantities
* Remove cart items
* Retrieve cart details

### Product Validation

Before adding a product to the cart, Cart Service communicates with Product Service to validate product information.

### Inventory Validation

Cart Service communicates with Inventory Service to check product availability before adding or updating cart items.

### Order Integration

Cart Service communicates with Order Service as part of the order workflow.

## Database

Cart Service uses MySQL for persistent storage.

```text
Database: ecomcartdb
```

Main entities:

* `Cart`
* `CartItem`

## API Examples

| Method | Endpoint    | Description         |
| ------ | ----------- | ------------------- |
| POST   | `/cart/...` | Add product to cart |
| GET    | `/cart/...` | Get customer's cart |
| PUT    | `/cart/...` | Update cart item    |
| DELETE | `/cart/...` | Remove cart item    |

> Exact endpoint paths can vary based on the controller configuration.

## Security

The service uses Spring Security and JWT authentication to secure APIs.

It also supports internal service authentication for secure communication between microservices.

## Service Port

```text
Cart Service: 8085
```

## Running the Service

Clone the repository and run:

```bash
mvn spring-boot:run
```

Or run the main Spring Boot application from your IDE.

Make sure MySQL and the required dependent microservices are running.

## Project Structure

```text
src/main/java/com/deepak/cartService
│
├── DTO
├── Entity
├── Repository
├── client
├── config
├── controller
├── security
└── service
```

## Role in E-Commerce System

The Cart Service manages the customer's shopping cart between product browsing and order placement.

```text
Browse Products
      ↓
Add to Cart
      ↓
Cart Service
      ↓
Update / Remove Items
      ↓
Place Order
      ↓
Order Service
```
