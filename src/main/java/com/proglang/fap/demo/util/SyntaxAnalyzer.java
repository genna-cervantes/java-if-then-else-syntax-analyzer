package com.proglang.fap.demo.util;


import java.util.ArrayList;
import java.util.List;

import com.proglang.fap.demo.exceptions.SyntaxException;
import com.proglang.fap.demo.models.Token;

public class SyntaxAnalyzer {

    ArrayList<Token> tokens;
    ArrayList<String> lexTokens;
    int lexCounter;

    public SyntaxAnalyzer(List<Token> tokens) {

        ArrayList<String> lexTokensCopy = new ArrayList<>();

        for (Token t : tokens) {
            lexTokensCopy.add(t.getToken());
        }

        this.tokens = new ArrayList<>(tokens);
        this.lexTokens = lexTokensCopy;
    }

    public void printTokens() {
        for (String s : lexTokens) {
            System.out.print(s + " ");
        }
    }

    public int findLine() {
        return tokens.get(lexCounter).getLineNumber() + 1;
    }

    // "if" "(" <condition> ")" <block> [<else-if>]
    public boolean parseIfThenElse() throws SyntaxException {

        String error;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("IF_KEYWORD")) {
            error = "Illegal start of if expression at line: " + findLine();

            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal start of if expression at line");
        }
        lexTokens.remove(0);
        lexCounter++;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("OPEN_PAREN")) {
            error = "Missing Open Parenthesis at line: " + findLine();

            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Missing Open Parenthesis at line");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (!parseBooleanExpression()) {
            error = "Illegal Boolean Expression at line: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Boolean Expression at line");
        }
        
        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("CLOSE_PAREN")) {
            error = "Wrong Close Paren at line: " + findLine();
            
            // throw new Exception(error);F
            throw new SyntaxException(findLine(), "Wrong Close Paren at line");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (!parseBlock()) {
            error = "Illegal Block: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Block");
        }
        
        // optional else if
        if (lexTokens.isEmpty()) {
            System.out.println("No Syntax Errors, Congratulations!");
            return true;
        }
        
        if (lexTokens.get(0).equals("ELSE_IF_KEYWORD")) {
            
            if (!parseElseIf()) {
                error = "Illegal Else If: " + findLine();
                
                // throw new Exception(error);
                throw new SyntaxException(findLine(), "Illegal Else If");
            }
        }
        
        // optional else after else if
        if (lexTokens.isEmpty()) {
            System.out.println("No Syntax Errors, Congratulations!");
            return true;
        }
        
        if (lexTokens.get(0).equals("ELSE_KEYWORD")) {
            
            if (!parseElse()) {
                error = "Illegal Else: " + findLine();
                
                // throw new Exception(error);
                throw new SyntaxException(findLine(), "Illegal Else");
            }
        }
        
        // should be the end
        if (!lexTokens.isEmpty()) {
            error = "Illegal Else If Condition: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Else If Condition");
        }
        
        System.out.println("No Syntax Errors, Congratulations!");
        return true;
    }
    
    // <else-if>          ::= "else if" "(" <condition> ")"  <block> [<else-if>] |  "else" <block>
    public boolean parseElseIf() throws SyntaxException {
        String error;
        
        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("ELSE_IF_KEYWORD")) {
            error = "Illegal start of else if expression at line: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal start of else if expression");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("OPEN_PAREN")) {
            error = "Missing Open Parenthesis at line: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Missing Open Parenthesis");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (!parseBooleanExpression()) {
            error = "Illegal Condition at line: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Condition");
        }
        
        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("CLOSE_PAREN")) {
            error = "Wrong Close Paren at line: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Wrong Close Parenthesis");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (!parseBlock()) {
            error = "Illegal Block: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Block");
        }
        
        // optional else if
        if (lexTokens.isEmpty()) {
            return true;
        }
        
        if (lexTokens.get(0).equals("ELSE_KEYWORD")) {
            return true;
        }
        
        // meron pang else if
        if (!parseElseIf()) {
            error = "Illegal Else If: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Else If");
        }
        
        return true;
    }
    
    //  "else" <block>
    public boolean parseElse() throws SyntaxException {
        String error;
        
        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("ELSE_KEYWORD")) {
            error = "Illegal start of else at line: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal start of else");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (!parseBlock()) {
            error = "Illegal Block: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Block");
        }
        
        return true;
    }
    
    // <block>        ::=  "{" <statement> "}"
    public boolean parseBlock() throws SyntaxException {
        String error;
        
        if (lexTokens.isEmpty()) {
            error = "missing block at line: " + findLine();
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "missing block");
        }
        
        if (!lexTokens.get(0).equals("OPEN_BRACKET")) {
            error = "Missing Open Bracket at line: " + findLine();
            
            //throw new Exception(error);
            throw new SyntaxException(findLine(), "Missing Open Bracket");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (!parseStatements()) {
            error = "Illegal Statement: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Statement");
        }
        // else kung whitespace check kung close bracket na valid pa rin nmn tapos return true na ung start ng if else na mag hhandle pag madoble ung close bracket?
        
        if (!lexTokens.get(0).equals("CLOSE_BRACKET")) {
            error = "Missing Close Bracket at line: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Missing Close Bracket");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        return true;
    }
    
    // <statements>    ::=  <statement> [ { <statement> } ] 
    public boolean parseStatements() throws SyntaxException {
        String error;
        
        if (lexTokens.isEmpty()) {
            error = "missing block at line: " + findLine();
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "missing block");
        }
        
        if (lexTokens.get(0).equals("CLOSE_BRACKET")) {
            return true;
        }
        
        if (parseStatement()) {
            
            if (!lexTokens.get(0).equals("SEMICOLON")) {
                lexCounter--;
                error = "missing semicolon: " + findLine();
                // throw new Exception(error);
                throw new SyntaxException(findLine(), "missing semicolon");
            }
            
            lexTokens.remove(0);
            lexCounter++;
            parseStatements();
        }
        
        return true;
    }
    
    // <statement>    ::=  " " | <condition> | <declaration> -- di keri ung " "
    public boolean parseStatement() throws SyntaxException {
        String error;
        
        if (parseAssignment()){
            return true;
        }
        
        // arithmetic expression nlng
        if (parseArithmeticExpression()) {
            return true;
        }
        
        if (parseDeclaration()) {
            return true;
        }
        
        error = "Illegal Block Statement: " + findLine();
        // throw new Exception(error);
        throw new SyntaxException(findLine(), "Illegal Block Statement");
    }
    
    // <assignment_expression> ::= <identifier> <assignment-operator> <expression>
    public boolean parseAssignment() throws SyntaxException{
        if (!lexTokens.get(0).equals("IDENT")){
            return false;
        }
        
        if (!lexTokens.get(1).equals("ASSIGNMENT_OP")){
            return false;
        }
        
        lexTokens.remove(0);
        lexCounter++;
        
        lexTokens.remove(0);
        lexCounter++;
        
        if (!parseAnyExpression()){
            return false;
        }
        
        return true;
    }
    
    // <declaration>  ::= <keyword> <identifier> ";"
    public boolean parseDeclaration() throws SyntaxException {
        String error;
        
        if (!lexTokens.get(0).equals("DECLARATION_KEYWORDS")) {
            error = "Illegal Start of Declaration: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Start of Declaration");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (!lexTokens.get(0).equals("IDENT")) {
            error = "Illegal Declaration: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Declaration");
        }
        lexTokens.remove(0);
        lexCounter++;
        
        if (lexTokens.get(0).equals("ASSIGNMENT_OP")) {
            lexTokens.remove(0);
            lexCounter++;
            
            // arithmetic expression | condition idk yet
            if (!parseArithmeticExpression()) {
                error = "Illegal Declaration: " + findLine();
                
                // throw new Exception(error);
                throw new SyntaxException(findLine(), "Illegal Declaration");
            }
        }
        
        if (!lexTokens.get(0).equals("SEMICOLON")) {
            error = "Illegal Declaration: " + findLine();
            
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "Illegal Declaration");
        }
        
        return true;
    }
    
    public boolean parseArithmeticExpression() throws SyntaxException {
        String error;
        
        if (lexTokens.isEmpty()) {
            error = "condition cannot be empty at line: " + findLine();
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "condition cannot be empty");
        }
        
        // try ( condition )
        if (lexTokens.get(0).equals("OPEN_PAREN")) {
            lexTokens.remove(0);
            lexCounter++;
            
            if (parseArithmeticExpression()) { // 1 // 2
                
                // possible mag out of bounds
                if (lexTokens.get(0).equals("CLOSE_PAREN")) {
                    lexTokens.remove(0);
                    lexCounter++;
                    return true;
                }
                
            }
        }
        
        // ((x+y) == 7)
        if (parseTerm()) {
            
            if (lexTokens.get(0).equals("ARITHMETIC_OP")) {
                lexTokens.remove(0);
                lexCounter++;
                
                if (parseTerm()) {
                    return true;
                } else {
                    // return false;
                    error = "illegal arithmetic expression at line: " + findLine();
                    // throw new Exception(error);
                    throw new SyntaxException(findLine(), "illegal arithmetic expression");
                }
            } else if (lexTokens.get(0).equals("SHORTHAND_ARITHMETIC_OP")) { // try <expression> <shorthand-arithmetic-operator>
                
                lexTokens.remove(0);
                lexCounter++;
                
                if (!lexTokens.get(0).equals("SEMICOLON")) {
                    error = "Illegal Arithmetic: " + findLine();
                    
                    // throw new Exception(error);
                    throw new SyntaxException(findLine(), "Illegal Arithmetic");
                }
                
                return true;
            } 
            return false;
        }
        
        return false;
    }
    
    public boolean parseTerm() throws SyntaxException {
        
        if (parseExpression()) {
            return true;
        }
        
        // if (parseArithmeticExpression()){
            //     return true;
            // }
            return false;
        }
        
        // <boolean_expression> :: = <expression> | “(“ <boolean_expression> “)” |  <boolean_expression> <comparison-operator> <boolean_expression>
        public boolean parseBooleanExpression() throws SyntaxException {
            
            String error;
            
        if (lexTokens.isEmpty()) {
            error = "condition cannot be empty at line: " + findLine();
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "condition cannot be empty");
        }
        
        if (parseAnyExpression()) {
            
            if (lexTokens.get(0).equals("COMPARISON_OP")) {
                lexTokens.remove(0);
                lexCounter++;
                
                if (!parseAnyExpression()) {
                    error = "illegal boolean expression at line: " + findLine();
                    // throw new Exception(error);   
                    throw new SyntaxException(findLine(), "illegal boolean expression");
                }
                
            }else if (lexTokens.get(0).equals("CLOSE_PAREN")) { // for single boolean expressions
                return true;
                
            }else{
                error = "illegal boolean expression at line: " + findLine();
                // throw new Exception(error);
                throw new SyntaxException(findLine(), "illegal boolean expression");
            }
        }
        
        
        if (lexTokens.get(0).equals("CLOSE_PAREN")) {
            
            if (!lexTokens.get(1).equals("OPEN_BRACKET")) {
                lexTokens.remove(0);
                lexCounter++;
                
                parseBooleanExpression();
            }
            
            return true;
        }
        
        
        return false;
    }
    
    public boolean parseAnyExpression() throws SyntaxException {
        
        if (lexTokens.get(0).equals("OPEN_PAREN")) {
            
            if (lexTokens.get(1).equals("IDENT") || lexTokens.get(1).equals("INTEGER_LIT")) {
                
                if (lexTokens.get(2).equals("ARITHMETIC_OP")) {
                    if (parseArithmeticExpression()) {
                        return true;
                    } else {
                        return false;
                    }
                }
                
                lexTokens.remove(0);
                lexCounter++;
                return true;
            }
            
            return false;
        }
        
        if (lexTokens.get(0).equals("IDENT") || lexTokens.get(0).equals("INTEGER_LIT")) {
            
            if (lexTokens.get(1).equals("ARITHMETIC_OP")) {
                if (parseArithmeticExpression()) {
                    return true;
                } else {
                    return false;
                }
            }
            
            lexTokens.remove(0);
            lexCounter++;
            return true;
        }
        
        return false;
    }
    
    
    // <operator>     ::=  <comparison-operator> | <arithmetic-operator> | <assignment-operator>
    public boolean parseOperator() throws SyntaxException {
        String error;
        
        if (lexTokens.isEmpty()) {
            // return false;
            error = "condition cannot be empty at line: " + findLine();
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "condition cannot be empty");
        }
        
        if (lexTokens.get(0).equals("COMPARISON_OP")) {
            return true;
        }
        
        if (lexTokens.get(0).equals("ARITHMETIC_OP")) {
            return true;
        }
        
        if (lexTokens.get(0).equals("ASSIGNMENT_OP")) {
            return true;
        }
        
        if (lexTokens.get(0).equals("SHORTCIRCUIT_OP")) {
            return true;
        }
        
        if (lexTokens.get(0).equals("BITWISE_OP")) {
            return true;
        }
        
        return false;
    }
    
    // <expression>   ::= <identifier> | <literal> 
    public boolean parseExpression() throws SyntaxException {
        String error;
        
        if (lexTokens.isEmpty()) {
            // return false;
            error = "expression cannot be empty at line: " + findLine();
            // throw new Exception(error);
            throw new SyntaxException(findLine(), "expression cannot be empty");
        }
        
        // try identifier
        if (lexTokens.get(0).equals("IDENT")) {
            lexTokens.remove(0);
            lexCounter++;
            return true;
        }
        
        if (lexTokens.get(0).equals("INTEGER_LIT")) {
            lexTokens.remove(0);
            lexCounter++;
            return true;
        }
        
        return false;
    }
    
}

