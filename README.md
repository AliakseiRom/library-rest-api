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
* **mvn clean**: Perform mvn clean install
* **Running project**: Run the Project

## API Endpoints

### Base URL
**http://localhost:8080**

### Swagger

* If you want to run this project using swagger,
  you should go to `/swagger-ui.html` endpoint

### Auth Controller
* POST `/authenticate`: Before calling endpoints from
  **Book Controller**,
  you need to authenticate by
  using **admin** as a username and a password
* Then you need to put access token
  in all Book Controller endpoints to
  authentication field (from bearer token tab, if you are using **Postman**)

### Book Controller
* GET `/book/{id}`: Get book by ID
* GET `/book/all`: Get all books
* GET `/book/isbn/{isbn}`: Get book by ISBN
* GET `/book/rent/{id}`: Rent a book by its ID
* GET `/book/return/{id}`: Return a rented book by its ID
* POST `/book/create`: Add a new book to the library
* PUT `/book/update/{id}`: Update an existing book by its ID
* DELETE `/book/delete/{id}`: Delete a book by its ID
* DELETE `/book/delete/all`: Delete all books