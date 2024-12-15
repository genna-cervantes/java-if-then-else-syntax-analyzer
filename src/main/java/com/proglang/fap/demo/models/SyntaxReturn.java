package com.proglang.fap.demo.models;

public class SyntaxReturn {
    private String errorCode;
    private int lineNumber;

    public SyntaxReturn(String errorCode, int lineNumber){
        this.errorCode = errorCode;
        this.lineNumber = lineNumber;
    }

    public String getErrorCode(){
        return this.errorCode;
    }

    public int getLineNumber(){
        return this.lineNumber;
    }

    public void setErrorCode(String errorCode){
        this.errorCode = errorCode;
    }

    public void setLineNumber(int lineNumber){
        this.lineNumber = lineNumber;
    }
}
