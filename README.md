# Loan Management System

The Loan Management System is a Spring Boot application that provides various functionalities related to managing accounts, clients, employees, and loan calculations.

## Technologies Used

- Spring Boot 2.5.9
- Java 11
- MySQL 8.0 (Database)
- Gradle (Build Tool)

## Setup Instructions

To run this application locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/Wis1/bank.git
   ```
2. Open the project in IntelliJ IDEA or any preferred IDE.

3. Configure MySQL database connection in application.properties:

properties
````
spring.datasource.url=jdbc:mysql://localhost:3306/db_name
spring.datasource.username=db_username
spring.datasource.password=db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
````
Replace db_name, db_username, and db_password with your MySQL database details.

Build and run the application using Gradle:

```
./gradlew bootRun
```
The application will start and be accessible at http://localhost:8080.
## Controllers and Endpoints
### AccountController
* GET /account/transfer-form: Shows transfer form for money transfer.
* POST /account/transfer: Transfers money between accounts.
* POST /account/client/{clientId}/sum/{sum}: Withdraws money from client's account.
* PUT /account/client/{clientId}/sum/{sum}: Deposits money into client's account.
* DELETE /account/client/{clientId}/account/{accountNumber}: Deletes client's account.
### ClientController
* GET /client/form: Shows client registration form.
* GET /client/signup: Shows sign up form.
* POST /client/new: Creates a new client.
* POST /client/register: Registers a new client user.
* GET /client: Retrieves all clients.
* GET /client/{clientId}: Retrieves details of a specific client.
* PUT /client/{clientId}/update: Updates client's information.
* DELETE /client/client/{clientId}: Deletes a client.
* GET /client/loanCalculator: Shows loan calculator form.
* POST /client/calculateLoan: Calculates loan based on input.
* GET /client/rate: Retrieves actual rate information.
### EmployeeController
* GET /employee/menu: Shows employee menu.
* POST /employee/register: Registers a new employee.
* POST /employee/account/{accountNumber}/client/{clientId}: Adds an account to a client.
* POST /employee/update: Updates employee's information.
### LoginController
* POST /login/login: Handles login authentication for both employees and clients.
## Additional Notes
Ensure MySQL Server is running and the database schema is set up as per entity mappings.
Cross-origin requests are allowed to facilitate integration with different domains.
.