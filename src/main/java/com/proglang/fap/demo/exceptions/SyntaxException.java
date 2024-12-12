package com.proglang.fap.demo.exceptions;

public class SyntaxException extends Exception {
    private int lineNumber;
    private String errorCode;
    
    public SyntaxException(int line, String errorCode){
        this.lineNumber = line;
        this.errorCode = errorCode;
    }

    public int getLine(){
        return lineNumber;
    }

    public String getErrorCode(){
        return errorCode;
    }
}
