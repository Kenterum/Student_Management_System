```markdown
# Spring Boot Student-Course Management System

This project is a web application built with Spring Boot, designed to manage students and their course enrollments. It supports CRUD  operations for both students and courses. 

Frontend: Thymeleaf
Storage: H2 database

## Prerequisites

- JDK 17
- Spring Boot version 3.2.3
- H2 Database
- Project Lombok

## Getting Started

To run this application locally, follow these steps:

1. **Clone the repository:**
   ```shell
   git clone https://github.com/SITE-ADA/as1-spring-boot-jpa-app-Kenterum.git
   ```

2. **Navigate to the project directory:**
   ```shell
   cd exampleproject
   ```

3. **Build the project:**
   ```shell
   ./gradlew clean build
   ```

4. **Run the application:**
   ```shell
   ./gradlew bootRun
   ```

5. **Access the application at:** [http://localhost:8080](http://localhost:8080)

## Features

- **Home Page:** Navigate to the home page at `http://localhost:8080` to access student and course management.

- **Student Management:**
  - **List Students:** `http://localhost:8080/students/index`
  - **Add Student:** `http://localhost:8080/students/create`
  - **Edit Student:** `http://localhost:8080/students/update/{id}`
  - **Delete Student:** `http://localhost:8080/students/delete/{id}`

- **Course Management:**
  - **View Courses:** `http://localhost:8080/courses/index`
  - **Create Course:** `http://localhost:8080/courses/create`
  - **Update Course:** `http://localhost:8080/courses/update/{id}`
  - **Delete Course:** `http://localhost:8080/courses/delete/{id}`



## Demo Video

For a detailed walkthrough of the application, watch the demo video:

Link to the video:
```
https://youtu.be/e9nBVn10knc
```
