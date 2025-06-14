package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.logging.Logger;

/**
 * Configuration for security audit logging
 */
@Configuration
public class AuditConfig {
    
    private static final Logger logger = Logger.getLogger(AuditConfig.class.getName());
    
    @Bean
    public SecurityAuditLogger securityAuditLogger() {
        return new SecurityAuditLogger();
    }
    
    /**
     * Simple security audit logger
     * In a real application, this would log to a secure audit trail
     */
    public static class SecurityAuditLogger {
        
        public void logSecurityEvent(String event, String username, String outcome) {
            // In production, this would log to a secure audit trail
            logger.info(String.format("SECURITY EVENT: %s, User: %s, Outcome: %s", 
                    event, username, outcome));
        }
        
        public void logAuthenticationAttempt(String username, boolean success) {
            logSecurityEvent("AUTHENTICATION", username, success ? "SUCCESS" : "FAILURE");
        }
        
        public void logAuthorizationCheck(String username, String resource, boolean granted) {
            logSecurityEvent("AUTHORIZATION", username, 
                    granted ? "ACCESS GRANTED to " + resource : "ACCESS DENIED to " + resource);
        }
    }
}