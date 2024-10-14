
import java.util.ArrayList;

public class SyntaxAnalyzer {

    ArrayList<Token> tokens;
    ArrayList<String> lexTokens;
    int lexCounter;

    public SyntaxAnalyzer(ArrayList<Token> tokens) {

        ArrayList<String> lexTokensCopy = new ArrayList<>();

        for (Token t : tokens) {
            lexTokensCopy.add(t.token);
        }

        this.tokens = tokens;
        this.lexTokens = lexTokensCopy;
    }

    public void printTokens() {
        for (String s : lexTokens) {
            System.out.print(s + " ");
        }
    }

    public int findLine() {
        return tokens.get(lexCounter).line + 1;
    }

    // "if" "(" <condition> ")" <block> [<else-if>]
    public boolean parseIfThenElse() throws Exception {

        // System.out.println("START PARSE");
        // System.out.println();
        String error;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("IF_KEYWORD")) {
            error = "Illegal start of if expression at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("OPEN_PAREN")) {
            error = "Missing Open Parenthesis at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (!parseBooleanExpression()) {
            error = "Illegal Boolean Expression at line: " + findLine();

            throw new Exception(error);
        }

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("CLOSE_PAREN")) {
            error = "Wrong Close Paren at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (!parseBlock()) {
            error = "Illegal Block: " + findLine();

            throw new Exception(error);
        }

        // optional else if
        if (lexTokens.isEmpty()) {
            System.out.println("No Syntax Errors, Congratulations!");
            return true;
        }

        if (lexTokens.get(0).equals("ELSE_IF_KEYWORD")) {

            if (!parseElseIf()) {
                error = "Illegal Else If: " + findLine();

                throw new Exception(error);
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

                throw new Exception(error);
            }
        }

        // should be the end
        if (!lexTokens.isEmpty()) {
            error = "Illegal Else If Condition: " + findLine();

            throw new Exception(error);
        }

        System.out.println("No Syntax Errors, Congratulations!");
        return true;
    }

    // <else-if>          ::= "else if" "(" <condition> ")"  <block> [<else-if>] |  "else" <block>
    public boolean parseElseIf() throws Exception {
        String error;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("ELSE_IF_KEYWORD")) {
            error = "Illegal start of else if expression at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("OPEN_PAREN")) {
            error = "Missing Open Parenthesis at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (!parseBooleanExpression()) {
            error = "Illegal Condition at line: " + findLine();

            throw new Exception(error);
        }

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("CLOSE_PAREN")) {
            error = "Wrong Close Paren at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (!parseBlock()) {
            error = "Illegal Block: " + findLine();

            throw new Exception(error);
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

            throw new Exception(error);
        }

        return true;
    }

    //  "else" <block>
    public boolean parseElse() throws Exception {
        String error;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("ELSE_KEYWORD")) {
            error = "Illegal start of else at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (!parseBlock()) {
            error = "Illegal Block: " + findLine();

            throw new Exception(error);
        }

        return true;
    }

    // <block>        ::=  "{" <statement> "}"
    public boolean parseBlock() throws Exception {
        String error;

        if (lexTokens.isEmpty()) {
            error = "missing block at line: " + findLine();
            throw new Exception(error);
        }

        if (!lexTokens.get(0).equals("OPEN_BRACKET")) {
            error = "Missing Open Bracket at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (!parseStatements()) {
            error = "Illegal Statement: " + findLine();

            throw new Exception(error);
        }
        // else kung whitespace check kung close bracket na valid pa rin nmn tapos return true na ung start ng if else na mag hhandle pag madoble ung close bracket?

        if (!lexTokens.get(0).equals("CLOSE_BRACKET")) {
            error = "Missing Close Bracket at line: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        return true;
    }

    // <statements>    ::=  <statement> [ { <statement> } ] 
    public boolean parseStatements() throws Exception {
        String error;

        if (lexTokens.isEmpty()) {
            error = "missing block at line: " + findLine();
            throw new Exception(error);
        }

        if (lexTokens.get(0).equals("CLOSE_BRACKET")) {
            return true;
        }

        if (parseStatement()) {

            if (!lexTokens.get(0).equals("SEMICOLON")) {
                lexCounter--;
                error = "missing semicolon: " + findLine();
                throw new Exception(error);
            }

            lexTokens.remove(0);
            lexCounter++;
            parseStatements();
        }

        return true;
    }

    // <statement>    ::=  " " | <condition> | <declaration> -- di keri ung " "
    public boolean parseStatement() throws Exception {
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
        throw new Exception(error);
    }

    // <assignment_expression> ::= <identifier> <assignment-operator> <expression>
    public boolean parseAssignment() throws Exception{
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
    public boolean parseDeclaration() throws Exception {
        String error;

        if (!lexTokens.get(0).equals("DECLARATION_KEYWORDS")) {
            error = "Illegal Start of Declaration: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (!lexTokens.get(0).equals("IDENT")) {
            error = "Illegal Declaration: " + findLine();

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;

        if (lexTokens.get(0).equals("ASSIGNMENT_OP")) {
            lexTokens.remove(0);
            lexCounter++;

            // arithmetic expression | condition idk yet
            if (!parseArithmeticExpression()) {
                error = "Illegal Declaration: " + findLine();

                throw new Exception(error);
            }
        }

        if (!lexTokens.get(0).equals("SEMICOLON")) {
            error = "Illegal Declaration: " + findLine();

            throw new Exception(error);
        }

        return true;
    }

    public boolean parseArithmeticExpression() throws Exception {
        String error;

        if (lexTokens.isEmpty()) {
            error = "condition cannot be empty at line: " + findLine();
            throw new Exception(error);
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
                    throw new Exception(error);
                }
            } else if (lexTokens.get(0).equals("SHORTHAND_ARITHMETIC_OP")) { // try <expression> <shorthand-arithmetic-operator>

                lexTokens.remove(0);
                lexCounter++;

                if (!lexTokens.get(0).equals("SEMICOLON")) {
                    error = "Illegal Arithmetic: " + findLine();

                    throw new Exception(error);
                }

                return true;
            } 
            return false;
        }

        return false;
    }

    public boolean parseTerm() throws Exception {

        if (parseExpression()) {
            return true;
        }

        // if (parseArithmeticExpression()){
        //     return true;
        // }
        return false;
    }

    // <boolean_expression> :: = <expression> | “(“ <boolean_expression> “)” |  <boolean_expression> <comparison-operator> <boolean_expression>
    public boolean parseBooleanExpression() throws Exception {

        String error;

        if (lexTokens.isEmpty()) {
            error = "condition cannot be empty at line: " + findLine();
            throw new Exception(error);
        }

        if (parseAnyExpression()) {

            if (lexTokens.get(0).equals("COMPARISON_OP")) {
                lexTokens.remove(0);
                lexCounter++;
    
                if (!parseAnyExpression()) {
                    error = "illegal boolean expression at line: " + findLine();
                    throw new Exception(error);   
                }

            }else if (lexTokens.get(0).equals("CLOSE_PAREN")) { // for single boolean expressions
                return true;

            }else{
                error = "illegal boolean expression at line: " + findLine();
                throw new Exception(error);
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

    public boolean parseAnyExpression() throws Exception {

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
    public boolean parseOperator() throws Exception {
        String error;

        if (lexTokens.isEmpty()) {
            // return false;
            error = "condition cannot be empty at line: " + findLine();
            throw new Exception(error);
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
    public boolean parseExpression() throws Exception {
        String error;

        if (lexTokens.isEmpty()) {
            // return false;
            error = "expression cannot be empty at line: " + findLine();
            throw new Exception(error);
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
