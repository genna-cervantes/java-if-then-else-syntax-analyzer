package com.proglang.fap.demo.aspects;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.proglang.fap.demo.annotations.RateLimited;
import com.proglang.fap.demo.exceptions.RateLimitException;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RateLimitAspect {

    // Store request counts for each client (identified by their IP address)
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    
    // Rate limit parameters
    private final long rateLimitDurationMillis = TimeUnit.MINUTES.toMillis(1);  // 1 minute
    private final int rateLimit = 100;  // 100 requests per minute

    // Apply to methods annotated with @RateLimited
    @Before("@annotation(rateLimited)") 
    public void rateLimit(RateLimited rateLimited) throws Exception {
        // Get the current HTTP request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        // Obtain the client IP address
        String clientIp = request.getRemoteAddr();  // Get the real client IP address
        long currentTime = System.currentTimeMillis();  // Get the current time

        // Update the request count for the client IP
        requestCounts.compute(clientIp, (key, currentCount) -> {
            if (currentCount == null || currentTime - currentCount.get() > rateLimitDurationMillis) {
                // Reset count if the time window has passed
                return new AtomicInteger(1);
            }
            return currentCount;
        });

        // Get the current count for the client
        AtomicInteger count = requestCounts.get(clientIp);
        
        // Check if the rate limit is exceeded
        if (count.get() > rateLimit) {
            throw new RateLimitException("Rate limit exceeded");
        }
        
        // Increment the request count
        count.incrementAndGet();
    }
}
