# eCommerce Application

This repository contains my implementation of an eCommerce application, demonstrating authentication, authorization, and security best practices in a Spring Boot environment.

## Project Overview
This application is developed in Java using Spring Boot, Hibernate ORM, and an H2 in-memory database, allowing a fresh state each time the application restarts.

The application consists of five main components:
- **Main Application**: Starts the application and configures Spring Boot.
- **Models**: Defines data entities such as `Cart`, `Item`, `User`, and `UserOrder`.
- **Repositories**: JPA repositories for database interactions.
- **Request Models**: Defines structures for incoming requests.
- **Controllers**: REST API endpoints for each model.

### Running the Application
To set up the application, import it into your preferred IDE and run it as a Spring Boot application. Once started, you can interact with the API using a REST client like Postman.

Some key API actions include:
- **Create a User**: `POST` request to `/api/user/create` with a JSON body:
   ```json
   {
       "username": "test"
   }
   ```

   Response:
   ```json
   {
       "id": 1,
       "username": "test"
   }
   ```

- **Add Items to Cart and Submit an Order**: Use the `ModifyCartRequest` class for adding items to a cart and submitting orders.

## Security Implementation
To secure the application, I have implemented authentication and authorization using Spring Security and JSON Web Tokens (JWT). Key features include:

1. **Password-Based Authentication**: User passwords are hashed and stored securely. For validation, additional fields are used to confirm password inputs.
2. **JWT for Authorization**: Tokens are used for secure access to endpoints, allowing only authorized users to access their data.

Key classes include:
- **Custom `UsernamePasswordAuthenticationFilter`**: Authenticates user login and provides a JWT in the response header.
- **Custom `BasicAuthenticationFilter`**: Verifies JWT on each request to secure endpoints.
- **UserDetailsService Implementation**: Manages user details retrieval by username, including hashed passwords.
- **WebSecurityConfigurerAdapter Implementation**: Configures security settings, attaches the user details service, and specifies secured and open endpoints.

Example login request:
```json
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

This returns an authorization token in the `Authorization` header. All subsequent requests require this token to access secure endpoints.

## Testing
This application includes unit tests covering at least 80% of the codebase, ensuring the functionality and security configurations are working as expected.
