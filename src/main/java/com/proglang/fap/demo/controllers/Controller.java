package com.proglang.fap.demo.controllers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proglang.fap.demo.models.Line;
import com.proglang.fap.demo.models.Request;

@RestController
public class Controller {

    @GetMapping("/hello")
    public String home() {
        return "Welcome to the API!";
    }

    @PostMapping("/analyze")
    public void analyze(@RequestBody Request rq) {
        ArrayList<Line> lines = rq.getLines();

        for (Line line: lines){
            System.out.println(line.getString());
        }
    }   
}
