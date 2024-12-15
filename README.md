# HabitLab-backend
HabitLab is an habit tracker application where you can start with your new habits!

This is a REST API developed in Java using Spring framework, based on the 3-layer pattern (controller, service, repository) and implementing practices such as the use of DTOs, centralized exception handling and secure configurations with Spring Security.

_****Key Features****_
- Security with Spring Security
  - Implementation of JWT to secure endpoints
  - Basic Auth also available
  - Defined roles for users with differentiated access
- Database Dockerization (and soon the whole backend)
  - Use of PostgreSQL as database management system
  - Use of docker-compose to simplify the development environment setup
- Custom Exception handling
  - Specific exceptions for common errors
  - Consistent responses with appropiate HTTP status codes and custom messages
- Java Streams
  - Use of Streams for manipulation of collections, such as filtering occurrences or calculating habit streaks
- Spring Data JPA
  - JPA-based repositories for data access
  - Custom queries where needed
- DTOs (Data Transfer Objects)
  - Use of DTOs to decouple persistance entities from the responses exposed in the endpoints
