# Java Web Developer Nanodegree Projects

This repository contains the four main projects I completed during the **Java Web Developer Nanodegree** program at Udacity. Each project demonstrates key concepts in Java web development, including RESTful APIs, microservices, data persistence, and cloud deployment.

---

## Table of Contents

1. [Project 1: Cloud Storage Application](#project-1-cloud-storage-application)
2. [Project 2: Vehicles API](#project-2-vehicles-api)
3. [Project 3: Critter Chronologer](#project-3-critter-chronologer)
4. [Project 4: eCommerce Application](#project-4-ecommerce-application)
5. [How to Run the Projects](#how-to-run-the-projects)
6. [License](#license)

---

## Project 1: Cloud Storage Application

**Directory:** [P01-cloudstorage](./P01-cloudstorage)

### Description

A RESTful API for a cloud storage application built using Spring Boot. The application allows users to securely store, retrieve, update, and delete files, notes, and credentials.

### Key Features

- **User Authentication:** Secure login system using hashing and salting.
- **File Management:** Upload and download files.
- **Note Management:** Create, read, update, and delete notes.
- **Credential Management:** Securely store credentials with encryption.
- **Responsive UI:** User-friendly interface with Thymeleaf templates.

### Technologies Used

- Java
- Spring Boot
- Thymeleaf
- MySQL
- HTML/CSS/JavaScript

---

## Project 2: Vehicles API

**Directory:** [P02-VehiclesAPI](./P02-VehiclesAPI)

### Description

A microservices-based application that provides a REST API to maintain vehicle data and pricing information. This project demonstrates how to build scalable and maintainable microservices using Spring Cloud.

### Key Features

- **Microservices Architecture:** Separate services for vehicle details, pricing, and location.
- **Service Discovery:** Implemented using Eureka server.
- **API Gateway:** Managed with Zuul for routing.
- **Fault Tolerance:** Hystrix for circuit breaker patterns.
- **External API Integration:** Retrieves pricing and location data from external APIs.

### Technologies Used

- Java
- Spring Boot
- Spring Cloud (Eureka, Zuul, Hystrix)
- RESTful APIs
- MySQL

---

## Project 3: Critter Chronologer

**Directory:** [P03-CritterChronologer](./P03-CritterChronologer)

### Description

A scheduling application for a pet grooming business. This project focuses on data persistence using Hibernate and JPA to manage complex data models and relationships.

### Key Features

- **Data Modeling:** Complex relationships between entities (users, pets, schedules).
- **ORM with Hibernate and JPA:** Mapping Java classes to database tables.
- **CRUD Operations:** Full create, read, update, delete functionality.
- **Advanced Queries:** Custom queries using JPQL.
- **Transaction Management:** Ensuring data consistency.

### Technologies Used

- Java
- Spring Boot
- Hibernate
- JPA
- MySQL

---

## Project 4: eCommerce Application

**Directory:** [P04-eCommerce-Application](./P04-eCommerce-Application)

### Description

An eCommerce application demonstrating CI/CD pipelines and cloud deployment strategies. The application is containerized with Docker and deployed on AWS Elastic Beanstalk.

### Key Features

- **Cloud Deployment:** Deployed on AWS Elastic Beanstalk.
- **Containerization:** Application packaged using Docker.
- **CI/CD Pipeline:** Automated builds and deployments using Jenkins.
- **Scalability:** Configured for horizontal scaling on AWS.
- **Monitoring:** Application performance monitored with AWS CloudWatch.

### Technologies Used

- Java
- Spring Boot
- Docker
- Jenkins
- AWS Elastic Beanstalk
- AWS RDS
- AWS CloudWatch

---

## How to Run the Projects

Each project contains its own `README.md` with detailed instructions on how to set up and run the application. Below is a general guide:

1. **Prerequisites:**
   - Java Development Kit (JDK) 8 or higher.
   - Maven or Gradle for dependency management.
   - MySQL or another relational database (if required by the project).
   - Docker (for Project 4).

2. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   ```

3. **Navigate to the Project Directory:**

   ```bash
   cd your-repo-name/P01-cloudstorage
   ```

4. **Build the Project:**

   ```bash
   mvn clean install
   ```

5. **Run the Application:**

   ```bash
   mvn spring-boot:run
   ```

6. **Access the Application:**

   - Open your web browser and navigate to `http://localhost:8080`.

---

## License

This repository is licensed under the [MIT License](./LICENSE).

---

Feel free to explore each project, and don't hesitate to contact me if you have any questions or feedback.

**Contact Information:**

- **Email:** jmartineztortola@gmail.com
- **LinkedIn:** [My LinkedIn Profile]https://www.linkedin.com/in/jorge-martinez-tortola-849411128/)
