package com.proglang.fap.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proglang.fap.demo.models.PaymentRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    // Constructor to initialize HttpClient
    public PaymentController() {
        // You can initialize your HttpClient here if needed for reuse
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        // Ensure you're sending the correct format for "amount"
        String requestBody = "{\"data\":{\"attributes\":{\"amount\":100000,\"description\":\"100 Credits\"}}}";

        // Log the request body for debugging purposes
        System.out.println("Request Body: " + requestBody);

        try {
            // Send the request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.paymongo.com/v1/links"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("authorization", "Basic c2tfdGVzdF9SaVNha1hXZUpBaGFHRU1TVnp6S0toRkc6dzczVHA1ZEUjQmE2X2pR")
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            return ResponseEntity.ok(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment intent creation");
        }
    }

    @GetMapping("/check-payment")
    public ResponseEntity<String> checkPayment(@RequestParam String linkId) {
        try {
            // Build URI for PayMongo API request
            URI uri = URI.create("https://api.paymongo.com/v1/links/" + linkId);
            
            // Create HTTP request to PayMongo API
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .header("authorization", "Basic c2tfdGVzdF9SaVNha1hXZUpBaGFHRU1TVnp6S0toRkc6dzczVHA1ZEUjQmE2X2pR")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    
            // Send the request and receive the response
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    
            // Log the raw response for debugging purposes
            System.out.println("PayMongo API response body: " + response.body());
    
            // Check if the response is successful and contains valid JSON
            if (response.statusCode() == 200) {
                // If the response is successful, return the JSON response as is
                return ResponseEntity.ok(response.body());
            } else {
                // Handle API error (non-200 status)
                return ResponseEntity.status(response.statusCode())
                        .body("Error from PayMongo API: " + response.body());
            }
        } catch (Exception e) {
            // Handle exceptions and return an error message
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }
}




