
import java.util.ArrayList;

public class SyntaxAnalyzer {

    ArrayList<Token> tokens;
    ArrayList<String> lexTokens;
    int lexCounter;

    public SyntaxAnalyzer(ArrayList<Token> tokens) {

        ArrayList<String> lexTokens = new ArrayList<>();

        for (Token t : tokens) {
            lexTokens.add(t.token);
        }

        this.tokens = tokens;
        this.lexTokens = lexTokens;
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
            // System.out.println(error);
            // return false;

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;
        // System.out.println("passed if");
        // printTokens();

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("OPEN_PAREN")) {
            error = "Missing Open Parenthesis at line: " + findLine();
            // System.out.println(error);
            // return false;

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;
        // System.out.println("passed open paren");
        // printTokens();

        if (!parseCondition()) {
            error = "Illegal Condition at line: " + findLine();
            // System.out.println(error);
            // return false;

            throw new Exception(error);
        }
        // System.out.println("passed condition");
        // printTokens();

        if (lexTokens.isEmpty() || !lexTokens.get(0).equals("CLOSE_PAREN")) {
            error = "Wrong Close Paren at line: " + findLine();
            // System.out.println(error);
            // return false;

            throw new Exception(error);
        }
        lexTokens.remove(0);
        lexCounter++;
        // System.out.println("passed closed paren");
        // printTokens();



        return true;
    }

    // <condition> ::= <expression> | “(“ <condition> “)” | <condition> <operator> <condition>
    public boolean parseCondition() throws Exception {
        String error;

        if (lexTokens.isEmpty()) {
            // return false;
            error = "condition cannot be empty at line: " + findLine();
            throw new Exception(error);
        }

        // try ( condition )
        if (lexTokens.get(0).equals("OPEN_PAREN")) {
            lexTokens.remove(0);
            lexCounter++;
            // System.out.println("passed open paren");
            // printTokens();

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
                }else{
                    // return false;
                    error = "illegal condition at line: " + findLine();
                    throw new Exception(error);
                }
            }

            // System.out.println("passed simple expression");
            // printTokens();
            return true;
        }

        error = "Illegal Condition at line: " + findLine();
        // return false;
        throw new Exception(error);
    }

    // <operator>     ::=  <comparison-operator> | <arithmetic-operator> | <assignment-operator>
    public boolean parseOperator() throws Exception {
        String error;

        if (lexTokens.isEmpty()){
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

        error = "Not an Operator at line: " + findLine();
        throw new Exception(error);
    }

    // <expression>   ::= <identifier> | <literal> 
    public boolean parseExpression() throws Exception {
        String error;

        if (lexTokens.isEmpty()){
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

        // System.out.println("returned false");
        // error = "Not an expression";
        // return false;
        error = "illegal expression at line: " + findLine();
        throw new Exception(error);
    }

}
