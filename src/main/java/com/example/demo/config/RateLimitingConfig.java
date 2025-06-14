package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class RateLimitingConfig implements WebMvcConfigurer {

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor(100, 1, TimeUnit.MINUTES); // 100 requests per minute
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor())
                .addPathPatterns("/hello/**"); // Apply to all hello endpoints
    }

    // Simple rate limiting interceptor
    public static class RateLimitInterceptor implements org.springframework.web.servlet.HandlerInterceptor {
        private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
        private final int capacity;
        private final int refillTokens;
        private final TimeUnit refillUnit;

        public RateLimitInterceptor(int capacity, int refillTokens, TimeUnit refillUnit) {
            this.capacity = capacity;
            this.refillTokens = refillTokens;
            this.refillUnit = refillUnit;
        }

        @Override
        public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, 
                                jakarta.servlet.http.HttpServletResponse response, 
                                Object handler) throws Exception {
            
            String clientIp = getClientIp(request);
            TokenBucket tokenBucket = buckets.computeIfAbsent(clientIp, 
                    k -> new TokenBucket(capacity, refillTokens, refillUnit));

            if (!tokenBucket.tryConsume()) {
                response.setStatus(429); // Too Many Requests
                response.getWriter().write("Rate limit exceeded. Try again later.");
                return false;
            }
            
            return true;
        }

        private String getClientIp(jakarta.servlet.http.HttpServletRequest request) {
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        }
    }

    // Token bucket algorithm for rate limiting
    private static class TokenBucket {
        private final int capacity;
        private final int refillTokens;
        private final long refillPeriodNanos;
        
        private int tokens;
        private long lastRefillTimestamp;

        public TokenBucket(int capacity, int refillTokens, TimeUnit refillUnit) {
            this.capacity = capacity;
            this.refillTokens = refillTokens;
            this.refillPeriodNanos = refillUnit.toNanos(1);
            this.tokens = capacity;
            this.lastRefillTimestamp = System.nanoTime();
        }

        public synchronized boolean tryConsume() {
            refill();
            
            if (tokens > 0) {
                tokens--;
                return true;
            }
            
            return false;
        }

        private void refill() {
            long now = System.nanoTime();
            long elapsedTime = now - lastRefillTimestamp;
            
            if (elapsedTime < refillPeriodNanos) {
                return;
            }
            
            int periodsElapsed = (int) (elapsedTime / refillPeriodNanos);
            int tokensToAdd = periodsElapsed * refillTokens;
            
            if (tokensToAdd > 0) {
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefillTimestamp = now;
            }
        }
    }
}