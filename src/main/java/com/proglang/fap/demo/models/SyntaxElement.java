package com.proglang.fap.demo.models;

abstract class SyntaxElement {
    private int lineNumber;

    public SyntaxElement(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}