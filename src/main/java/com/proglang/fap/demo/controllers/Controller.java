package com.proglang.fap.demo.controllers;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proglang.fap.demo.exceptions.SyntaxException;
import com.proglang.fap.demo.models.Line;
import com.proglang.fap.demo.models.Request;
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

    @PostMapping("/analyze")
    public ResponseEntity<SyntaxReturn> analyze(@RequestBody Request rq) {
        ArrayList<Line> lines = rq.getLines();
        ArrayList<Token> tokens = new ArrayList<>();
        
        for (Line line: lines){
            System.out.println(line.getString());

            LexicalAnalyzer la = new LexicalAnalyzer();
            ArrayList<String> tokensInLine = la.tokenizeString(line.getString());

            for (String t : tokensInLine) {
                Token token = new Token(line.getLineNumber(), t);
                tokens.add(token);
            }
        }

        SyntaxAnalyzer sa = new SyntaxAnalyzer(tokens);
        try {
            sa.parseIfThenElse();
        } catch (SyntaxException e) {
            System.out.println(e.getMessage());
            SyntaxReturn sr = new SyntaxReturn(e.getErrorCode(), e.getLine());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sr);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SyntaxReturn("No Error", 0));
    }   
}
