package com.proglang.fap.demo.models;

public class Token {
    private int line;
    private String token;

    public Token(int line, String token){
        this.line = line;
        this.token = token;
    }

    public int getLine(){
        return this.line;
    }

    public String getToken(){
        return this.token;
    }
}
