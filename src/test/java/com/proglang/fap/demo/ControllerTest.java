package com.proglang.fap.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proglang.fap.demo.models.Line;
import com.proglang.fap.demo.models.SyntaxRequest;
import com.proglang.fap.demo.models.SyntaxReturn;
import com.proglang.fap.demo.models.Token;
import com.proglang.fap.demo.util.LexicalAnalyzer;
import com.proglang.fap.demo.util.SyntaxAnalyzer;
import com.proglang.fap.demo.controllers.Controller;
import com.proglang.fap.demo.exceptions.SyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;

public class ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LexicalAnalyzer lexicalAnalyzer;

    @Mock
    private SyntaxAnalyzer syntaxAnalyzer;

    @InjectMocks
    private Controller controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAnalyzeSuccess() throws Exception {
        // Setup mock data for the request
        Line line1 = new Line(1, "if (x > 0) {");
        Line line2 = new Line(2, "    y = 10;");
        SyntaxRequest syntaxRequest = new SyntaxRequest();
        syntaxRequest.setLines(Arrays.asList(line1, line2));

        // Mock LexicalAnalyzer behavior
        when(lexicalAnalyzer.tokenizeString(any())).thenReturn(Arrays.asList("if", "(", "x", ">", "0", ")"));

        // Mock SyntaxAnalyzer behavior
        doNothing().when(syntaxAnalyzer).parseIfThenElse();

        // Send the POST request and validate the response
        mockMvc.perform(post("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(syntaxRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().json("{\"errorCode\":\"No Error\",\"line\":0}"));

        // Verify interactions with mocks
        verify(lexicalAnalyzer, times(2)).tokenizeString(any());
        verify(syntaxAnalyzer, times(1)).parseIfThenElse();
    }

    @Test
    void testAnalyzeFailure() throws Exception {
        // Setup mock data for the request
        Line line1 = new Line(1, "if (x > 0 {");
        Line line2 = new Line(2, "y = 10;");
        SyntaxRequest syntaxRequest = new SyntaxRequest();
        syntaxRequest.setLines(Arrays.asList(line1, line2));

        // Mock LexicalAnalyzer behavior
        when(lexicalAnalyzer.tokenizeString(any())).thenReturn(Arrays.asList("if", "(", "x", ">", "0", "{"));

        // Mock SyntaxAnalyzer behavior to throw SyntaxException
        doThrow(new SyntaxException("SYNTAX_ERROR", 1)).when(syntaxAnalyzer).parseIfThenElse();

        // Send the POST request and validate the response
        mockMvc.perform(post("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(syntaxRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().json("{\"errorCode\":\"SYNTAX_ERROR\",\"line\":1}"));

        // Verify interactions with mocks
        verify(lexicalAnalyzer, times(2)).tokenizeString(any());
        verify(syntaxAnalyzer, times(1)).parseIfThenElse();
    }
}

