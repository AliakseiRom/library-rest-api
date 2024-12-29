# Library rest api
This project is a REST API for managing a library system. 
It allows users to perform CRUD operations on books, 
rent and return books, and handle authentication.
The project is built using Java with Spring Boot and Maven.

## Getting Started

Postman collection is located in the root folder **library_rest_api.postman_collection.json**

### Requirements
To build and run this project, you will need the following:

* **JDK**: Install Java Develompment Kit 19
* **Maven**: Install Apache Maven 4.0.0
* **Postgres**: Create database with your postgres user. Use command: `CREATE DATABASE library WITH OWNER postgres`
* **application.yml**: Set up your credentials to Postgres DB with your username and password
* **mvn clean install**: Perform mvn clean install
* **Running project**: Run the Project
* **Initializing Roles**: Roles will automatically be added to the database after running the project

## API Endpoints

### Base URL
**http://localhost:8080**

### Swagger

* If you want to run this project using swagger,
  you should go to `/swagger-ui.html` endpoint

### Auth Controller
* POST `/register`: Before calling authenticate endpoint
  you need to register by entering username, password
  and roles that you need, for example `ROLE_ADMIN`
* POST `/authenticate`: Before calling other endpoints from
  **Book Controller**,
  you need to authenticate by
  using username and password that you used in register endpoint
* Then you need to put access token
  in all Book Controller endpoints into authorization header: `Authorization: Bearer <access_token>`

### Book Controller
* GET `/books/{id}`: Get book by ID
* GET `/books`: Get all books
* GET `/books/isbn/{isbn}`: Get book by ISBN
* GET `/books/rent/{id}`: Rent a book by its ID
* GET `/books/return/{id}`: Return a rented book by its ID
* GET `/books/rental_info/{id}`: Get book's rental info by book's id
* POST `/books`: Add a new book to the library
* PUT `/books/{id}`: Update an existing book by its ID
* DELETE `/books/{id}`: Delete a book by its ID
* DELETE `/books`: Delete all books