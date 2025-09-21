🔐 JWT-Based Authentication in MessUp
This project implements stateless authentication using JWT (JSON Web Token) and Spring Security. Below is a complete overview of the authentication and authorization mechanism used.

✅ Overview

    The authentication flow uses:
    Spring Security for managing security configuration and filters.
    JWT (JSON Web Token) for stateless authentication.
    BCrypt for secure password encoding.
    CustomUserDetailsService for loading user data from the database.

🔄 Authentication Flow
    1. User Logs In
    
        Endpoint: POST /api/auth/login
        Body:
            {
              "username": "user1",
              "password": "password"
            }
    2. Backend Authentication Process

    AuthenticationManager triggers the authentication.
    DaoAuthenticationProvider uses CustomUserDetailsService to load the user.
    Password is verified using BCryptPasswordEncoder.    
    On success, a JWT token is generated and returned to the client.

📦 JWT Token

    Contains: username, issuedAt, expiration
    Signed using: HMAC SHA256
    Valid for: 10 hours

🛡 Authorization Flow
    1. Client Sends Authenticated Request
        Adds JWT token in the header:
        Authorization: Bearer <token>

    2. JwtAuthenticationFilter
        Extracts the JWT from the header.
        Uses JwtService to:
        
          1.Decode the token.
          2.Validate its signature and expiration.
          3.Extract the username.
        
        Loads user using CustomUserDetailsService.
        Creates an Authentication object and sets it in the SecurityContext.

⚙️ Configuration
    SecurityConfig.java
    Disables CSRF.
    Makes /api/auth/** public.
    Requires authentication for all other endpoints.
    
    Registers:
    
        1.JwtAuthenticationFilter
        2.DaoAuthenticationProvider     
        3.Stateless session policy
    
    JwtService.java

        1.Generates and verifies JWT tokens using a secure secret key.
        2.Uses the latest jjwt API (verifyWith(...)).
    
    CustomUserDetailsService.java

        1.Loads User from database using UserRepository.
        2.Returns a Spring-compatible UserDetails object.
