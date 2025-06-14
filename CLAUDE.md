# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.2.0 REST API application built with Java 17 that provides a comprehensive greeting service with full CRUD operations. The application demonstrates production-ready practices including security, validation, testing, and automated deployment to AWS.

## Common Development Commands

### Build and Run
```bash
# Build the project
mvn clean install

# Run the application (starts on port 8080)
mvn spring-boot:run

# Package for deployment
mvn clean package
```

### Testing
```bash
# Run all tests (JUnit + Cucumber)
mvn test

# Run only unit tests
mvn test -Dtest=*Test

# Run Cucumber BDD tests
mvn test -Dtest=CucumberTestRunner

# Generate test reports
mvn verify
```

### Deployment
The application deploys to AWS Lambda via GitHub Actions. Manual deployment:
```bash
cd src/terraform
terraform init
terraform plan
terraform apply
```

## Architecture

The application follows a layered architecture with these key components:

### API Layer (`HelloController.java`)
- REST endpoints for greeting operations
- Spring Security with Basic Authentication
- Input validation and error handling
- Supports both `/hello` (basic) and full CRUD operations

### Security Layer (`config/SecurityConfig.java`)
- Spring Security configuration with Basic Auth
- XSS protection and security headers
- CORS configuration for cross-origin requests
- Content Security Policy enforcement

### Service/Repository Layers
- Business logic in service classes
- Data access through repository pattern
- Thread-safe in-memory storage implementation

### Configuration Management
- Environment-specific properties (dev/prod)
- Security configuration with environment variables
- Logging configuration via `logback-spring.xml`

## Key Features

### API Endpoints Structure
- `GET /hello` - Basic hello world
- `GET /hello/{name}` - Personalized greeting  
- `GET /hello/all` - List all greetings
- `POST /hello` - Create greeting
- `PUT /hello/{id}` and `PUT /hello/name/{name}` - Update operations
- `DELETE /hello/{id}` and `DELETE /hello/name/{name}` - Delete operations

### Testing Strategy
- **JUnit 5**: Unit and integration tests
- **Cucumber**: BDD tests with feature files in `src/test/resources/features/`
- **REST Assured**: API endpoint testing
- Comprehensive test coverage including edge cases and validation

### Security Implementation
- Spring Security with Basic Authentication
- XSS protection filters
- Input validation with regex patterns
- Security headers and HTTPS enforcement in production

## Environment Configuration

### Development (`application-dev.properties`)
- Port 8080
- Debug logging enabled
- Relaxed security for development

### Production (`application-prod.properties`)
- Environment variables for sensitive data
- SSL/HTTPS enforcement
- Production logging levels

## Infrastructure

### AWS Deployment Architecture
- **Lambda**: Java 17 runtime hosting the Spring Boot application
- **API Gateway**: HTTP API with throttling and CORS
- **CloudFront**: CDN with custom domain support
- **Route 53**: DNS management
- **WAF**: Web Application Firewall protection
- **ACM**: SSL certificate management

### Terraform Configuration
All infrastructure defined in `src/terraform/` with modules for:
- API Gateway security and custom domains
- CloudFront distribution with WAF
- KMS encryption for environment variables
- Route 53 DNS records

## CI/CD Pipeline

GitHub Actions workflow includes:
1. Security scanning (Trivy, OWASP dependency check)
2. Maven build and test execution
3. Terraform deployment to AWS
4. Automated rollback on failure

## Development Notes

- The application uses Lombok for reducing boilerplate code
- All endpoints return JSON responses with proper HTTP status codes
- Input validation uses Spring Validation with custom regex patterns
- Thread-safe operations for concurrent access
- Comprehensive error handling with structured error responses