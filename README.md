
# Personal Finance Tracker API

## 📌 Overview
This is a **Spring Boot & MongoDB** RESTful API for managing personal finances, allowing users to track expenses, set budgets, and generate reports. The application implements **JWT authentication** and **role-based access control (RBAC)** for security.

## 🚀 Features
- **User Authentication & Role-based Access** (Admin & User)
- **Expense & Income Tracking** (CRUD operations)
- **Budget Management** (Set limits & notifications)
- **Financial Reports** (Analyze income vs. expenses)
- **Goals & Savings Tracking** (Set and track savings goals)
- **Multi-Currency Support** (Exchange rate integration)
- **Notifications & Alerts** (Spending warnings & bill reminders)

---

## 🛠️ Setup Instructions
### 1️⃣ **Prerequisites**
Ensure you have the following installed:
- **Java 17** or later
- **Maven** (Check installation with `mvn -version`)
- **MongoDB** (Local or [MongoDB Atlas](https://www.mongodb.com/cloud/atlas))
- **Postman** (Optional for API testing)

### 2️⃣ **Clone the Repository**
```sh
git clone https://github.com/heshanjeewantha/personal-finance-tracker.git
cd personal-finance-tracker
```

### 3️⃣ **Configure Application Properties**
Update `src/main/resources/application.properties` with your MongoDB credentials:
```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster-url>/personal-finance
spring.data.mongodb.database=personal-finance
jwt.secret=<your-secure-secret-key>
```

### 4️⃣ **Build & Run the Application**
```sh
mvn clean install
mvn spring-boot:run
```
The API will be available at: `http://localhost:8070`

---

## 📌 API Documentation

### 🔑 **Authentication**
#### 1️⃣ **Register User**
```http
POST /api/auth/register
```
**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePass123",
  "role": "USER"
}
```

#### 2️⃣ **Login (Get JWT Token)**
```http
POST /api/auth/login
```
**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePass123"
}
```
_Response:_ JWT token for authentication.

---

### 💰 **Transactions**
#### 1️⃣ **Create Transaction**
```http
POST /api/transactions/add
Authorization: Bearer <JWT_TOKEN>
```
**Request Body:**
```json
{
  "type": "EXPENSE",
  "amount": 50.0,
  "category": "Food",
  "description": "Lunch at cafe"
}
```

#### 2️⃣ **Get All Transactions**
```http
GET /api/transactions
Authorization: Bearer <JWT_TOKEN>
```

---

### 📊 **Budgets**
#### 1️⃣ **Create Budget**
```http
POST /api/budgets/create
Authorization: Bearer <JWT_TOKEN>
```
**Request Body:**
```json
{
  "category": "Food",
  "amount": 500.0,
  "startDate": "2025-03-01",
  "endDate": "2025-03-31"
}
```

#### 2️⃣ **Get All Budgets**
```http
GET /api/budgets
Authorization: Bearer <JWT_TOKEN>
```

---

### 💵 **Currency Conversion**
#### 1️⃣ **Convert Currency**
```http
GET /api/currency/convert?from=USD&to=EUR&amount=100
```

---

## ✅ Running Tests
### 🔹 **Unit Tests**
Run unit tests with:
```sh
mvn test
```

### 🔹 **Integration Tests**
Run integration tests to verify API interactions:
```sh
mvn verify
```

---

## 📈 Performance & Security Testing
- Use **JMeter** to evaluate API load handling
- Use **OWASP ZAP** for automated security vulnerability scanning

---

## 🏆 Contributors
👤 **Heshan Jeewantha**  
📧 Contact: [GitHub Profile](https://github.com/heshanjeewantha)

---

## 📜 License
This project is licensed under the **MIT License**.
