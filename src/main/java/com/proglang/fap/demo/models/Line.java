package com.proglang.fap.demo.models;

public class Line extends SyntaxElement{
    private String str;  

    public Line(int lineNumber, String str){
        super(lineNumber);
        this.str = str;
    }

    public String getString(){
        return this.str;
    }
}
