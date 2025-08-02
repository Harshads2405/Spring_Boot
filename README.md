# Spring_Boot
Spring Boot projects 
This project is a simple CRUD (Create, Read, Update, Delete) application built with Spring Boot,
using Maven for dependency management, Spring Data JPA for data access, and Hibernate as the JPA provider.

Basic Project Structure:

spring-boot-crud-app/
 src/
`  main/
     java/
       com/example/demo/
         controller/
         entity/
         repository/
         service/
       DemoApplication.java
 resources/
   application.properties
 .gitignore
   pom.xml
 README.md

Key Layers:
1. Entity
2. Repository
3. Service
4. Controller

 Technologies Used:
- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven

Sample Endpoints:
  GET /api/users
  POST /api/users
  PUT /api/users/{id}
  DELETE /api/users/{id}
