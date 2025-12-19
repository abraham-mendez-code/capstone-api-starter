# Capstone API Starter

This repository contains the starter backend API for the EasyShop capstone project — a Spring Boot application providing product and category management, authentication, and shopping cart capabilities.

This API is built as a **capstone backend service** using Spring Boot, JWT authentication, MySQL, and follows RESTful conventions.

---

## 🧠 Project Overview

This project is designed for the **capstone backend component** of a web application that allows users to:

- Authenticate using JWT tokens
- Browse products by category or filter/search
- Manage categories (admin only)
- Add items to a shopping cart
- Persist cart items between sessions

The backend includes a fully functional API and will later be extended with features such as shopping cart persistence.

---

## 🚀 Features

✔ User registration & login with JWT authentication  
✔ Products listing and search/filter support  
✔ Category CRUD endpoints (admin only)  
✔ Structured controller + DAO + service layers  
✔ Unit tests for key components  
✔ Secure routes with Spring Security  
✔ Database initialization script included

---

## 📦 Getting Started

### 🧾 Prerequisites

Before running the app, make sure you have installed:

- Java 17+
- MySQL
- Maven

Clone this repository:

```sh
git clone https://github.com/abraham-mendez-code/capstone-api-starter.git
cd capstone-api-starter
```

## 🗄️ Database Setup

Open MySQL Workbench (or preferred client).

Run the provided database script located at:

/database/easyshop.sql

This will create the easyshop database with sample products and users.

Default Users:

user, admin, george

Password: password

## ⚙️ Configuration

Copy application.properties and set your database credentials:

spring:
datasource:
url: jdbc:mysql://localhost:3306/easyshop
username: <your_mysql_user>
password: <your_mysql_password>

## ▶️ Running the Application
./mvnw spring-boot:run


Server runs on:

http://localhost:8080

## 🔐 Authentication
Register
POST /register
Content-Type: application/json


Example body:

{
"username": "admin",
"password": "password",
"confirmPassword": "password",
"role": "ADMIN"
}

Login
POST /login
Content-Type: application/json


Example body:

{
"username": "admin",
"password": "password"
}


Returns a JWT token which must be included in all future authenticated requests using:

Authorization: Bearer <token>

## 📌 Endpoints Summary
Public
Method	URL	Description
GET	/products	List all products
GET	/products/{id}	Get product by ID
GET	/products?filters...	Search/Filter products
Categories (Admin Only)
Method	URL	Description
GET	/categories	List categories
GET	/categories/{id}	Get category by ID
POST	/categories	Create a category
PUT	/categories/{id}	Update a category
DELETE	/categories/{id}	Remove a category

## 📄 Postman (Optional)

You can import a Postman collection for:

✔ Login
✔ Protected routes
✔ Category CRUD
✔ Product search & filters

(Be sure to add the JWT token to authenticated requests.)

## 📸 Application Screenshots

Screenshots showcasing the EasyShop application UI interacting with this API.

### Product Browsing & Search
![product browsing.png](src/test/resources/screenshots/product%20browsing.png)
This screen shows the product listing page, where users can browse products, filter by category, and apply search criteria such as price range and subcategory. The backend API handles filtering logic and returns the appropriate product data.

### Shopping Cart
![shopping cart.png](src/test/resources/screenshots/shopping%20cart.png)
The shopping cart screen demonstrates how logged-in users can add products to their cart. Cart data is persisted in the database and retrieved through the API so items remain available across sessions.

## 💡 Interesting Code Highlight

### Dynamic Product Search & Filtering Logic
One of the most interesting pieces of this project is the dynamic product search functionality, which allows users to filter products using multiple optional query parameters such as category, price range, and subcategory.
```
public List<Product> search(Integer categoryId, BigDecimal minPrice,
BigDecimal maxPrice, String subCategory) {

    String sql = "SELECT * FROM products WHERE 1=1";
    List<Object> params = new ArrayList<>();

    if (categoryId != null) {
        sql += " AND category_id = ?";
        params.add(categoryId);
    }

    if (minPrice != null) {
        sql += " AND price >= ?";
        params.add(minPrice);
    }

    if (maxPrice != null) {
        sql += " AND price <= ?";
        params.add(maxPrice);
    }

    if (subCategory != null && !subCategory.isBlank()) {
        sql += " AND sub_category LIKE ?";
        params.add("%" + subCategory + "%");
    }

    return jdbcTemplate.query(sql, params.toArray(), productRowMapper);
}
```
This approach ensures flexibility while maintaining readable and maintainable SQL logic. Unit tests were added to validate correct behavior across all filter combinations.

## 🔮 Future Versions

The following features were discussed as potential improvements for future versions of the application. These ideas were inspired by common functionality found in large e-commerce platforms such as Amazon.

### 📈 Feature Roadmap (Ranked by Priority)
#### 1. Order History & Checkout System

#### Description:
Allow users to check out their shopping cart and create persistent orders, with the ability to view past purchases.

#### 2. Product Reviews & Ratings

#### Description:
Enable users to leave reviews and ratings on products.

#### 3. Inventory & Stock Management

#### Description:
Automatically update product stock levels when orders are placed.

#### 4. Wishlist / Favorites

#### Description:
Allow users to save products to a wishlist for later viewing.

#### 5. Enhanced Security & Auditing

#### Description:
Track changes made by administrators and improve security visibility.

## 🧠 Summary

This project establishes a strong backend foundation for an e-commerce application, focusing on clean architecture, secure APIs, and extensibility. The planned future enhancements demonstrate how the current design can scale to support more advanced real-world features.
