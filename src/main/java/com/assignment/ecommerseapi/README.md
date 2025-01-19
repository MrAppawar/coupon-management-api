
# Coupon Management API

This is a Spring Boot project that provides a RESTful API for managing and applying coupons in an e-commerce system.
It allows you to create, retrieve, delete coupons, and apply them to shopping carts.

## Table of Contents
1. [Installation](#installation)
2. [Usage](#usage)
3. [Configuration](#configuration)
4. [Features](#features)
5. [Contributing](#contributing)
6. [License](#license)

## Installation

To get started with this project, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/coupon-management-api.git
    ```

2. Navigate to the project directory:
    ```bash
    cd coupon-management-api
    ```

3. Make sure you have Java 17 or higher installed. You can verify this by running:
    ```bash
    java -version
    ```

4. Build the project with Maven:
    ```bash
    ./mvnw clean install
    ```

5. Run the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```

    Your app will be running on `http://localhost:9494`.

## Usage

Once the application is running, you can access the API endpoints using `http://localhost:9494`.

Here are some example API calls:

- **Create a Coupon:**
    ```bash
    POST http://localhost:8080/coupon
    Content-Type: application/json
    {
      "couponType": "productwisecoupon",
      "discount": 20,
      "productId": 1,
    }
    ```

- **Get Coupon by ID:**
    ```bash
    GET http://localhost:8080/coupon/{id}
    ```

- **Get All Coupons:**
    ```bash
    GET http://localhost:8080/coupon
    ```

- **Delete Coupon:**
    ```bash
    DELETE http://localhost:8080/coupon/{id}
    ```

- **Get Applicable Coupons:**
    ```bash
    POST http://localhost:8080/coupon/applicablecoupons
    Content-Type: application/json
    {
      "items": [
        { "productId": 1, "quantity": 2, "price":100 },
        { "productId": 3, "quantity": 1, "price":100 }
      ]
    }
    ```

- **Apply Coupon to Cart:**
    ```bash
    PUT http://localhost:8080/coupon/applycoupon/{id}
    Content-Type: application/json
     {
      "items": [
        { "productId": 1, "quantity": 2, "price":100 },
        { "productId": 3, "quantity": 1, "price":100 }
      ]
    }
    ```

## Configuration

You can configure the application by editing the `application.properties` or `application.yml` file located in the `src/main/resources/` directory.

Example configurations:

```properties(H2 Database)
spring:
    h2:
         console.enabled: true
    datasource:
         url: jdbc:h2:mem:test
         driverClassName: org.h2.Driver
         username: sa
         password: password
    jpa: 
     database-platform: org.hibernate.dialect.H2Dialect
     hibernate:
      ddl-auto: update
