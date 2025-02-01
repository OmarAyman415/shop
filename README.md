# Shop Project

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)
![Java](https://img.shields.io/badge/Java-17-orange)

This is a **Shop Project** built using Spring Boot and PostgreSQL. It serves as a backend for managing shop-related functionalities, including product inventory, orders, customers, and more.

---

## Features

- **Product Management**: Add, update, and delete products.
- **Order Processing**: Handle customer orders and maintain order history.
- **Customer Management**: Keep track of customer information.
- **Database Integration**: Persistent storage using PostgreSQL.
- **RESTful API**: Clean and well-structured API endpoints.
- **Environment Configuration**: Supports `.env` files using `spring-dotenv`.
- **Exception Handling**: Robust error handling with meaningful messages.
- **Spring Security**: Basic authentication and role-based access control.
---

## Technologies Used

- **Java 17**
- **Spring Boot 3.4.1**
  - Spring Data JPA
  - Spring Security
  - Spring Web
  - Spring Validation
- **PostgreSQL 14**
- **Maven**
- **Lombok** (1.18.30)

---

## Prerequisites

1. Install [Java 17](https://openjdk.org/).
2. Install [PostgreSQL](https://www.postgresql.org/download/).
3. Install [Maven](https://maven.apache.org/).
---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/Omarayman415/shop.git
cd shop-project
```

### 2. Configure the Database

1. Create a PostgreSQL database:
```sql
CREATE DATABASE shop_db;
```

2. Update `application.properties` or `application.yml` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/shop
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

3. Optionally, create a `.env` file for environment-specific configurations:
```env
DATABASE_URL=jdbc:postgresql://localhost:5432/shop_db
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_password
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:9193` by default.
---
