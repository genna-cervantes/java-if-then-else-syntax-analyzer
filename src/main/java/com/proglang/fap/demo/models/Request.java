package com.proglang.fap.demo.models;

import java.util.ArrayList;

public class Request {
    private ArrayList<Line> lines = new ArrayList<>();

    public void setCode(String code){
        String[] codeLines = code.split("\n");

        for (int i = 0; i < codeLines.length; i++){
            lines.add(new Line(i, codeLines[i]));
        }
    }

    public ArrayList<Line> getLines(){
        return this.lines;
    }
}
