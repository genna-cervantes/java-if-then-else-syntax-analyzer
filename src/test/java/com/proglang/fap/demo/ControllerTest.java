package com.proglang.fap.demo;

import com.proglang.fap.demo.models.SyntaxRequest;
import com.proglang.fap.demo.models.SyntaxReturn;
import com.proglang.fap.demo.util.LexicalAnalyzer;
import com.proglang.fap.demo.util.SyntaxAnalyzer;
import com.proglang.fap.demo.exceptions.SyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.proglang.fap.demo.controllers.Controller;
import com.proglang.fap.demo.models.Line;

@SpringBootTest
public class ControllerTest {

    @MockBean
    private LexicalAnalyzer lexicalAnalyzer;

    @MockBean
    private SyntaxAnalyzer syntaxAnalyzer;

    @Autowired
    private Controller controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHello() {
        String result = controller.home();
        assertEquals("Welcome to the API!", result);
    }

    @Test
    void testAnalyze_SuccessfulSyntax() throws SyntaxException {
        // Prepare mock data
        SyntaxRequest request = new SyntaxRequest();
        request.setCode("if (x > 10) {int x = 1;}");

        // Mock LexicalAnalyzer behavior
        when(lexicalAnalyzer.tokenizeString(anyString())).thenReturn(new ArrayList<>());

        // Mock SyntaxAnalyzer behavior
        doNothing().when(syntaxAnalyzer).parseIfThenElse();

        // Call the controller method
        ResponseEntity<SyntaxReturn> response = controller.analyze(request);

        // Verify the result
        assertEquals(202, response.getStatusCodeValue()); // Accepted status
        assertEquals("No Error", response.getBody().getErrorCode());
    }

    @Test
    void testAnalyze_SyntaxError() throws SyntaxException {
        // Prepare mock data
        SyntaxRequest request = new SyntaxRequest();
        request.setCode("if (x > 10 {");

        // Mock LexicalAnalyzer behavior
        when(lexicalAnalyzer.tokenizeString(anyString())).thenReturn(new ArrayList<>());

        // Mock SyntaxAnalyzer to throw a SyntaxException
        SyntaxException syntaxException = new SyntaxException(1, "Syntax error");
        doThrow(syntaxException).when(syntaxAnalyzer).parseIfThenElse();

        // Call the controller method
        ResponseEntity<SyntaxReturn> response = controller.analyze(request);

        // Verify the result
        assertEquals(202, response.getStatusCodeValue()); // Accepted status
        assertEquals("Syntax error", response.getBody().getErrorCode());
        assertEquals(1, response.getBody().getLineNumber());
    }
}

