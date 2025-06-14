package com.example.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.demo.model.HelloMessage;
import com.example.demo.service.GreetingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class GreetingHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final GreetingService greetingService;
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("[a-zA-Z0-9\\s]+");
    
    // Initialize service with repository
    static {
        GreetingDao greetingDao = new com.example.demo.repository.GreetingDaoImpl();
        greetingService = new GreetingServiceImpl(greetingDao);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String httpMethod = event.getHttpMethod();
        String path = event.getPath();
        Map<String, String> pathParams = event.getPathParameters() != null ? event.getPathParameters() : new HashMap<>();
        String body = event.getBody();

        // Add security headers to all responses
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Content-Type-Options", "nosniff");
        headers.put("X-Frame-Options", "DENY");
        headers.put("X-XSS-Protection", "1; mode=block");
        headers.put("Content-Security-Policy", "default-src 'self'");
        headers.put("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        try {
            switch (httpMethod) {
                case "GET":
                    if (path.equals("/hello")) {
                        return new APIGatewayProxyResponseEvent()
                                .withStatusCode(200)
                                .withHeaders(headers)
                                .withBody("Hello World");
                    }
                    if (path.equals("/hello/all")) {
                        return new APIGatewayProxyResponseEvent()
                                .withStatusCode(200)
                                .withHeaders(headers)
                                .withBody(mapper.writeValueAsString(greetingService.getAllGreetings()));
                    }
                    if (path.startsWith("/hello/name/")) {
                        String name = pathParams.get("name");
                        
                        // Validate input
                        if (name == null || !ALPHANUMERIC_PATTERN.matcher(name).matches()) {
                            return createErrorResponse(headers, 400, "Invalid name format");
                        }
                        
                        return new APIGatewayProxyResponseEvent()
                                .withStatusCode(200)
                                .withHeaders(headers)
                                .withBody("Hello " + name);
                    }
                    break;

                case "POST":
                    if (path.equals("/hello")) {
                        // Validate and sanitize input
                        GreetingRequest req = validateAndParseRequest(body, headers);
                        if (req == null) {
                            return createErrorResponse(headers, 400, "Invalid request format");
                        }
                        
                        Greeting greeting = greetingService.createGreeting(req);
                        return new APIGatewayProxyResponseEvent()
                                .withStatusCode(200)
                                .withHeaders(headers)
                                .withBody(mapper.writeValueAsString(greeting));
                    }
                    break;

                case "PUT":
                    if (path.startsWith("/hello/id/")) {
                        String id = pathParams.get("id");
                        
                        // Validate ID
                        if (id == null || !ALPHANUMERIC_PATTERN.matcher(id).matches()) {
                            return createErrorResponse(headers, 400, "Invalid ID format");
                        }
                        
                        // Validate and sanitize input
                        GreetingRequest req = validateAndParseRequest(body, headers);
                        if (req == null) {
                            return createErrorResponse(headers, 400, "Invalid request format");
                        }
                        
                        Optional<Greeting> updatedGreeting = greetingService.updateGreeting(id, req);
                        if (updatedGreeting.isEmpty()) {
                            return createErrorResponse(headers, 404, "Greeting not found");
                        }
                        
                        return new APIGatewayProxyResponseEvent()
                                .withStatusCode(200)
                                .withHeaders(headers)
                                .withBody(mapper.writeValueAsString(updatedGreeting.get()));
                    }
                    break;

                case "DELETE":
                    if (path.startsWith("/hello/id/")) {
                        String id = pathParams.get("id");
                        
                        // Validate ID
                        if (id == null || !ALPHANUMERIC_PATTERN.matcher(id).matches()) {
                            return createErrorResponse(headers, 400, "Invalid ID format");
                        }
                        
                        boolean deleted = greetingService.deleteGreeting(id);
                        return new APIGatewayProxyResponseEvent()
                                .withStatusCode(deleted ? 204 : 404)
                                .withHeaders(headers)
                                .withBody(deleted ? "" : "Greeting not found");
                    }
                    break;
            }
        } catch (Exception e) {
            context.getLogger().log("Error processing request: " + e.getMessage());
            return createErrorResponse(headers, 500, "Internal server error");
        }

        return createErrorResponse(headers, 400, "Bad Request");
    }
    
    private GreetingRequest validateAndParseRequest(String body, Map<String, String> headers) {
        try {
            if (body == null || body.isEmpty()) {
                return null;
            }
            
            GreetingRequest request = mapper.readValue(body, GreetingRequest.class);
            
            // Basic validation
            if (request.getName() == null || request.getMessage() == null || 
                !ALPHANUMERIC_PATTERN.matcher(request.getName()).matches()) {
                return null;
            }
            
            return request;
        } catch (Exception e) {
            return null;
        }
    }
    
    private APIGatewayProxyResponseEvent createErrorResponse(Map<String, String> headers, int statusCode, String message) {
        Map<String, String> errorBody = Collections.singletonMap("error", message);
        try {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(headers)
                    .withBody(mapper.writeValueAsString(errorBody));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(headers)
                    .withBody("{\"error\":\"" + message + "\"}");
        }
    }
}