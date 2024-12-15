package com.proglang.fap.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proglang.fap.demo.annotations.RateLimited;
import com.proglang.fap.demo.exceptions.SyntaxException;
import com.proglang.fap.demo.models.Line;
import com.proglang.fap.demo.models.SyntaxRequest;
import com.proglang.fap.demo.models.SyntaxReturn;
import com.proglang.fap.demo.models.Token;
import com.proglang.fap.demo.util.LexicalAnalyzer;
import com.proglang.fap.demo.util.SyntaxAnalyzer;


@RestController
public class Controller {

    @GetMapping("/hello")
    public String home() {
        return "Welcome to the API!";
    }

    @RateLimited
    @PostMapping(value = "/analyze", produces = "application/json")
    public ResponseEntity<SyntaxReturn> analyze(@RequestBody SyntaxRequest rq) {
        // ArrayList<Line> lines = rq.getLines();
        // ArrayList<Token> tokens = new ArrayList<>();

        final List<Line> lines = rq.getLines(); // Assume this is populated
        final Queue<Token> tokens = new ConcurrentLinkedQueue<>();

        processLinesConcurrently(lines, tokens);
        
        LexicalAnalyzer la = new LexicalAnalyzer();
        for (Line line: lines){
            ArrayList<String> tokensInLine = la.tokenizeString(line.getString());

            for (String t : tokensInLine) {
                Token token = new Token(line.getLineNumber(), t);
                tokens.add(token);
            }
        }

        SyntaxAnalyzer sa = new SyntaxAnalyzer(tokens);
        for (Token t: tokens){
            System.out.println(t.getToken());
        }
        try {
            sa.parseIfThenElse();
        } catch (SyntaxException e) {
            System.out.println("may error");
            SyntaxReturn sr = new SyntaxReturn(e.getErrorCode(), e.getLine());
            System.out.println(sr.getErrorCode());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(sr);
        }
        
        System.out.println("no error");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SyntaxReturn("No Error", 0));
    }   

    // concurrency
    private void processLinesConcurrently(List<Line> lines, Queue<Token> tokens) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        BlockingQueue<Token> tokenQueue = new LinkedBlockingQueue<>();

        for (Line line : lines) {
            executor.submit(() -> {
                try {
                    LexicalAnalyzer la = new LexicalAnalyzer(); // Thread-local instance
                    ArrayList<String> tokensInLine = la.tokenizeString(line.getString());

                    // Add tokens to the queue in order
                    for (String t : tokensInLine) {
                        Token token = new Token(line.getLineNumber(), t);
                        tokenQueue.put(token);  // Blocking operation ensures order
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line " + line.getLineNumber() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown(); // Initiate shutdown
        try {
            if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force shutdown if tasks don't complete in time
                System.err.println("Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Executor interrupted: " + e.getMessage());
        }
    }

}
