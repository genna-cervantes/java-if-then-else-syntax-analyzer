package com.proglang.fap.demo.models;

public class SyntaxReturn {
    private String errorCode;
    private int lineNumber;

    public SyntaxReturn(String errorCode, int lineNumber){
        this.errorCode = errorCode;
        this.lineNumber = lineNumber;
    }
}
