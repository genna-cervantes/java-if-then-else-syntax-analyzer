
import java.util.ArrayList;

public class SyntaxAnalyzer {

    ArrayList<String> lexTokens;

    public SyntaxAnalyzer(ArrayList<String> lexTokens) {
        this.lexTokens = lexTokens;
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

        if (!lexTokens.get(0).equals("OPEN_PAREN")) {
            error = "Missing Open Parenthesis";
            System.out.println(error);
            return false;
        }
        lexTokens.remove(0);

        if (!parseCondition()) {
            error = "Illegal Condition";
            System.out.println(error);
            return false;
        }

        if (!lexTokens.get(0).equals("CLOSE_PAREN")){
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

            if (parseCondition()) { // 1 // 2
                if (lexTokens.get(0).equals("CLOSE_PAREN")) {
                    lexTokens.remove(0);
                    return true;
                }
            }
        }

        // try <condition> <operator> <condition>
        // <operator>     ::=  <comparison-operator> | <arithmetic-operator> | <assignment-operator>
        if (parseExpression()) {
            
            if (parseOperator()) {
                lexTokens.remove(0);

                if (parseCondition()) {
                    return true;
                }
            }

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
