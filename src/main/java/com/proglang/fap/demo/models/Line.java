package com.proglang.fap.demo.models;

public class Line {
    private int lineNumber;
    private String str;  

    public Line(int line, String str){
        this.lineNumber = line;
        this.str = str;
    }

    public int getLineNumber(){
        return this.lineNumber;
    }

    public String getString(){
        return this.str;
    }
}
