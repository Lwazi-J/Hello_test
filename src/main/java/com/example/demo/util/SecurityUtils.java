package com.example.demo.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for security-related operations
 */
public class SecurityUtils {
    
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    /**
     * Generates a secure random token
     * 
     * @param length Length of the token in bytes
     * @return Base64 encoded secure random token
     */
    public static String generateSecureToken(int length) {
        byte[] token = new byte[length];
        SECURE_RANDOM.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
    
    /**
     * Performs a constant-time comparison of two strings to prevent timing attacks
     * 
     * @param a First string
     * @param b Second string
     * @return True if the strings are equal
     */
    public static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        
        byte[] aBytes = a.getBytes();
        byte[] bBytes = b.getBytes();
        
        if (aBytes.length != bBytes.length) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }
        
        return result == 0;
    }
}