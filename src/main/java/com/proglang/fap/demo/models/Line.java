package com.proglang.fap.demo.models;

public class Line {
    private int line;
    private String str;  

    public Line(int line, String str){
        this.line = line;
        this.str = str;
    }

    public int getLine(){
        return this.line;
    }

    public String getString(){
        return this.str;
    }
}
