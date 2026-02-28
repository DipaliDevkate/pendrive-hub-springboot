# Pendrive Hub 🛒

**Pendrive Hub** is a full-stack e-commerce web application built with **Spring Boot**, **Thymeleaf**, **MySQL**, and **Bootstrap**.  
It allows users to browse products, add them to the cart, buy products immediately, and track their orders. Admins can manage products and view all orders.

---

## Features ✅

### User Features
- Register and login securely
- Browse products with images and descriptions
- Add products to cart
- Buy products immediately
- Checkout all cart items at once
- View order history with details (quantity, total price, order date)

### Admin Features
- Secure admin login
- Add, edit, and delete products
- View all user orders with details

---

## Technologies Used 🛠

- **Backend:** Java 17, Spring Boot 3, Spring MVC, Spring Data JPA  
- **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap 5  
- **Database:** MySQL  
- **Tools:** Maven, Git, Spring Tools Suite (STS) / Eclipse  

---

## Installation & Setup 💻

1. **Clone the repository:**

```bash
git clone https://github.com/<your-username>/pendrive-hub.git
cd pendrive-hub

Import project into IDE:

Use Spring Tools Suite (STS) or Eclipse

Import as a Maven project

Database Setup:

Create a MySQL database named pendrive_hub_db

Import the SQL file:

mysql -u root -p < db/pendrive-hub.sql

Configure application properties:

Open src/main/resources/application.properties

Update MySQL username, password, and database:

spring.datasource.url=jdbc:mysql://localhost:3306/pendrive_hub_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

Run the application:

mvn spring-boot:run

Access the application:

User dashboard: http://localhost:8080/

Admin login: http://localhost:8080/admin/login

Project Structure 📂
pendrive-hub/
│
├─ src/main/java/com/example/demo/
│   ├─ controller/    # Controllers
│   ├─ entity/        # JPA Entities
│   ├─ repository/    # Spring Data Repositories
│   └─ service/       # Services
│
├─ src/main/resources/
│   ├─ templates/     # Thymeleaf HTML files
│   ├─ static/        # CSS, JS, images
│   └─ application.properties
│
├─ db/
│   └─ pendrive-hub.sql  # MySQL DB backup
│
└─ pom.xml
