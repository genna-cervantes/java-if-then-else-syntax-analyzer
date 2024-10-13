
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

        System.out.println("parsing if then else");
        String error;

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("IF_KEYWORD")) {
            error = "Illegal start of if else then expression at line: " + findLine();

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

        if (!parseCondition()) {
            error = "Illegal Condition at line: " + findLine();

            throw new Exception(error);
        }
        System.out.println("passed condition");
        printTokens();

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

        if (lexTokens.get(0).equals("CLOSE_BRACKET")){
            return true;
        }

        // x;
        // y

        if (parseStatement()){
            // if ; check pa ng statement -- so ndi tatanggalin sa statement ung ;
            
            if (lexTokens.get(0).equals("SEMICOLON")){
                lexTokens.remove(0);
                System.out.println("parsing next statement");
                parseStatements();
            }else{
                error = "missing semicolon: " + findLine();
                throw new Exception(error);
            }
        }

        return true;
    }

    // <statement>    ::=  " " | <condition> | <declaration> -- di keri ung " "
    public boolean parseStatement() throws Exception {
        String error;

        printTokens();
        if (parseCondition()) {
            return true;
        }
        System.out.println("failed condition in statement");

        if (parseDeclaration()) {
            return true;
        }

        error = "Illegal Block Statement: " + findLine();
        throw new Exception(error);
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

        return true;
    }

    // <condition> ::= <expression> | “(“ <condition> “)” | <condition> <operator> <condition>
    public boolean parseCondition() throws Exception {
        String error;

        if (lexTokens.isEmpty()) {
            error = "condition cannot be empty at line: " + findLine();
            throw new Exception(error);
        }

        // try ( condition )
        if (lexTokens.get(0).equals("OPEN_PAREN")) {
            lexTokens.remove(0);
            lexCounter++;

            if (parseCondition()) { // 1 // 2

                // possible mag out of bounds
                if (lexTokens.get(0).equals("CLOSE_PAREN")) {
                    lexTokens.remove(0);
                    lexCounter++;
                    return true;
                }

            }
        }

        // try <condition> <operator> <condition>
        // <operator>     ::=  <comparison-operator> | <arithmetic-operator> | <assignment-operator>
        if (parseExpression()) {

            if (parseOperator()) {
                lexTokens.remove(0);
                lexCounter++;
                // System.out.println("passed operator");
                // printTokens();

                if (parseCondition()) {
                    return true;
                } else {
                    // return false;
                    error = "illegal condition at line: " + findLine();
                    throw new Exception(error);
                }
            }

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
            System.out.println("returned true from here");
            lexTokens.remove(0);
            return true;
        }

        if (lexTokens.get(0).equals("INTEGER_LIT")) {
            System.out.println("returned true from here 2");
            lexTokens.remove(0);
            lexCounter++;
            return true;
        }

        return false;
    }

}
