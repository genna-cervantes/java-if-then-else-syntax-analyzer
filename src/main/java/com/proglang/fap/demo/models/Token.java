package com.proglang.fap.demo.models;

public class Token extends SyntaxElement {
    private String token;

    public Token(int lineNumber, String token){
        super(lineNumber);
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }
}
