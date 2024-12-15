package com.proglang.fap.demo.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SyntaxRequest {
    private ArrayList<Line> lines = new ArrayList<>();

    // functional programming
    public void setCode(String code){
        AtomicInteger index = new AtomicInteger();
        lines.addAll(
            Arrays.stream(code.split("\n"))
            .map(line -> new Line(index.getAndIncrement(), line)) // increment index for each line
            .toList()
        );
    }

    public ArrayList<Line> getLines(){
        return this.lines;
    }
}
