
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private String IF_KEYWORD = "\\bif\\b";
    private String ELSE_IF_KEYWORD = "\\belse if\\b";
    private String ELSE_KEYWORD = "\\belse\\b";
    private String DECLARATION_KEYWORDS = "\\b(int|boolean|char)\\b";
    private String BOOLEAN_LIT = "\\b(true|false)\\b";
    private String COMPARISON_OP = ">=|<=|==|!=|>|<";
    private String SHORTHAND_ARITHMETIC_OP = "(\\+\\+|--)";
    private String ARITHMETIC_OP = "[+\\-*/]";
    private String ASSIGNMENT_OP = "=";
    private String SHORTCIRCUIT_OP = "\\|\\||&&";
    private String BITWISE_OP = "\\||&";
    private String IDENT = "[a-zA-Z$_][a-zA-Z0-9$_]*";
    private String INTEGER_LIT = "[0-9]+";
    private String OPEN_PAREN = "\\(";
    private String CLOSE_PAREN = "\\)";
    private String OPEN_BRACKET = "\\{";
    private String CLOSE_BRACKET = "\\}";
    private String SEMICOLON = ";";

    private String[] tokens = {
        "IF_KEYWORD",
        "ELSE_IF_KEYWORD",
        "ELSE_KEYWORD",
        "DECLARATION_KEYWORDS",
        "BOOLEAN_LIT",
        "COMPARISON_OP",
        "SHORTHAND_ARITHMETIC_OP",
        "ARITHMETIC_OP",
        "ASSIGNMENT_OP",
        "SHORTCIRCUIT_OP",
        "BITWISE_OP",
        "IDENT",
        "INTEGER_LIT",
        "OPEN_PAREN",
        "CLOSE_PAREN",
        "OPEN_BRACKET",
        "CLOSE_BRACKET",
        "SEMICOLON"};

    private String[] tokenPatterns = {
        IF_KEYWORD,
        ELSE_IF_KEYWORD,
        ELSE_KEYWORD,
        DECLARATION_KEYWORDS,
        BOOLEAN_LIT,
        COMPARISON_OP,
        SHORTHAND_ARITHMETIC_OP,
        ARITHMETIC_OP,
        ASSIGNMENT_OP,
        SHORTCIRCUIT_OP,
        BITWISE_OP,
        IDENT,
        INTEGER_LIT,
        OPEN_PAREN,
        CLOSE_PAREN,
        OPEN_BRACKET,
        CLOSE_BRACKET,
        SEMICOLON
    };

    public ArrayList<String> tokenizeString(String input){
        
        ArrayList<String> recognizedTokens = new ArrayList<>();

        String regex = String.join("|", tokenPatterns);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String matchedToken = matcher.group();
            for (int i = 0; i < tokenPatterns.length; i++) {
                if (matchedToken.matches(tokenPatterns[i])) {
                    if (!matchedToken.trim().isEmpty()) { 
                        recognizedTokens.add(tokens[i]);
                    }
                    break;
                }
            }
        }

        return recognizedTokens;
    }
}
