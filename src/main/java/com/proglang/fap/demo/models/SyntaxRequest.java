package com.proglang.fap.demo.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SyntaxRequest {
    private String code;

    public SyntaxRequest() {
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    
    // functional programming
    public ArrayList<Line> getLines() {
        System.out.println("code");
        System.out.println(code);

        ArrayList<Line> lines = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        
        // Normalize line breaks to \n and split lines
        lines.addAll(
            Arrays.stream(code.split("\\r?\\n"))  // This handles both \n and \r\n line endings
                .map(line -> new Line(index.getAndIncrement(), line))  // Increment index for each line
                .toList()
        );

        System.out.println("lines");
        for (Line l: lines){
            System.out.println(l.getString());
        }
        return lines;
    }
}
