
import java.util.ArrayList;

public class SyntaxAnalyzer {

    ArrayList<String> lexTokens;

    public SyntaxAnalyzer(ArrayList<Token> tokens) {

        ArrayList<String> lexTokens = new ArrayList<>();

        for (Token t : tokens) {
            lexTokens.add(t.token);
        }

        this.lexTokens = lexTokens;
    }

    public void printTokens() {
        for (String s : lexTokens) {
            System.out.print(s + " ");
        }
    }

    // "if" "(" <condition> ")" <block> [<else-if>]
    public boolean parseIfThenElse() {

        System.out.println("parsing if then else");
        String error;

        if (!lexTokens.get(0).equals("IF_KEYWORD")) {
            error = "Illegal start of if else then expression";
            System.out.println(error);
            return false;
        }
        lexTokens.remove(0);
        System.out.println("passed if");
        printTokens();

        if (!lexTokens.get(0).equals("OPEN_PAREN")) {
            error = "Missing Open Parenthesis";
            System.out.println(error);
            return false;
        }
        lexTokens.remove(0);
        System.out.println("passed open paren");
        printTokens();

        if (!parseCondition()) {
            error = "Illegal Condition";
            System.out.println(error);
            return false;
        }
        System.out.println("passed condition");
        printTokens();

        // make this a utility or just find a better way to handle out of bounds
        try {

            if (!lexTokens.get(0).equals("CLOSE_PAREN")) {
                error = "Wrong Close Paren";
                System.out.println(error);
                return false;
            }
            lexTokens.remove(0);
            System.out.println("passed closed paren");
            printTokens();

        } catch (Exception e) {
            error = "Wrong Close Paren";
            System.out.println(error);
            return false;
        }

        return true;
    }

    // <condition> ::= <expression> | “(“ <condition> “)” | <condition> <operator> <condition>
    public boolean parseCondition() {
        String error;

        // try ( condition )
        if (lexTokens.get(0).equals("OPEN_PAREN")) {
            lexTokens.remove(0);
            System.out.println("passed open paren");
            printTokens();

            if (parseCondition()) { // 1 // 2

                try {
                    // possible mag out of bounds
                    if (lexTokens.get(0).equals("CLOSE_PAREN")) {
                        lexTokens.remove(0);
                        return true;
                    }

                } catch (Exception e) {
                    return false;
                }
            }
        }

        // try <condition> <operator> <condition>
        // <operator>     ::=  <comparison-operator> | <arithmetic-operator> | <assignment-operator>
        if (parseExpression()) {

            if (parseOperator()) {
                lexTokens.remove(0);
                System.out.println("passed operator");
                printTokens();

                if (parseCondition()) {
                    return true;
                }
            }

            System.out.println("passed simple expression");
            printTokens();
            return true;
        }

        error = "Illegal Condition";
        return false;
    }

    // <operator>     ::=  <comparison-operator> | <arithmetic-operator> | <assignment-operator>
    public boolean parseOperator() {
        String error;

        if (lexTokens.get(0).equals("COMPARISON_OP")) {
            return true;
        }

        if (lexTokens.get(0).equals("ARITHMETIC_OP")) {
            return true;
        }

        if (lexTokens.get(0).equals("ASSIGNMENT_OP")) {
            return true;
        }

        error = "Not an Operator";
        return false;
    }

    // <expression>   ::= <identifier> | <literal> 
    public boolean parseExpression() {
        String error;

        // try identifier
        if (lexTokens.get(0).equals("IDENT")) {
            lexTokens.remove(0);
            return true;
        }

        if (lexTokens.get(0).equals("INT_LIT")) {
            lexTokens.remove(0);
            return true;
        }

        error = "Not an expression";
        return false;
    }

}
