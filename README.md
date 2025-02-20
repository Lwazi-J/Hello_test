# Greetings API

A Spring Boot REST API for managing greetings with full CRUD operations and proper layered architecture.

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Error Handling](#error-handling)

## Features
- Complete CRUD operations for greetings
- Three-layer architecture (Controller, Service, DAO)
- Input validation
- Error handling
- Thread-safe operations
- Logging
- URL-based responses
- Unit tests for all layers

## Technologies
- Java 11
- Spring Boot 2.7.x
- Maven
- Lombok
- SLF4J/Logback
- JUnit 5
- Spring Validation

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── controller/
│   │       │   └── GreetingController.java
│   │       ├── service/
│   │       │   ├── GreetingService.java
│   │       │   └── GreetingServiceImpl.java
│   │       ├── dao/
│   │       │   ├── GreetingDao.java
│   │       │   └── GreetingDaoImpl.java
│   │       ├── model/
│   │       │   ├── Greeting.java
│   │       │   ├── GreetingRequest.java
│   │       │   └── GreetingResponse.java
│   │       └── DemoApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/example/demo/
            ├── controller/
            ├── service/
            └── dao/
```

## Getting Started

### Prerequisites
- JDK 11 or later
- Maven 3.6 or later
- Your favorite IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/greetings-api.git
cd greetings-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080

## API Documentation

### Basic Endpoints

#### Hello World
```
GET /api/hello
Response: "Hello World"
```

#### Personalized Hello
```
GET /api/hello/{name}
Response: "Hello {name}"
```

### Greeting Management

#### Create Greeting
```http
POST /api/greetings
Content-Type: application/json

{
    "name": "John",
    "message": "Hello from the API!"
}

Response: 200 OK
{
    "id": "1",
    "message": "Hello John! Your message: Hello from the API!",
    "url": "http://localhost:8080/api/greetings/1"
}
```

#### Get All Greetings
```http
GET /api/greetings

Response: 200 OK
[
    {
        "id": "1",
        "message": "Hello John! Your message: Hello from the API!",
        "url": "http://localhost:8080/api/greetings/1"
    }
]
```

#### Get Greeting by ID
```http
GET /api/greetings/{id}

Response: 200 OK
{
    "id": "1",
    "message": "Hello John! Your message: Hello from the API!",
    "url": "http://localhost:8080/api/greetings/1"
}
```

#### Update Greeting
```http
PUT /api/greetings/{id}
Content-Type: application/json

{
    "name": "John Updated",
    "message": "Updated message"
}

Response: 200 OK
{
    "id": "1",
    "message": "Hello John Updated! Your updated message: Updated message",
    "url": "http://localhost:8080/api/greetings/1"
}
```

#### Delete Greeting
```http
DELETE /api/greetings/{id}

Response: 204 No Content
```

## Testing

### Using cURL
```bash
# Create greeting
curl -X POST http://localhost:8080/api/greetings \
  -H "Content-Type: application/json" \
  -d '{"name": "John", "message": "Test message"}'

# Get all greetings
curl http://localhost:8080/api/greetings

# Get specific greeting
curl http://localhost:8080/api/greetings/1

# Update greeting
curl -X PUT http://localhost:8080/api/greetings/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "John Updated", "message": "Updated message"}'

# Delete greeting
curl -X DELETE http://localhost:8080/api/greetings/1
```

### Using Maven
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=GreetingControllerTest
```

## Error Handling

The API uses standard HTTP status codes:
- 200: Success
- 201: Created
- 204: No Content (successful deletion)
- 400: Bad Request (validation errors)
- 404: Not Found
- 500: Internal Server Error

Error Response Format:
```json
{
    "errors": {
        "fieldName": "Error message"
    }
}
```

## Contributing
1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details
