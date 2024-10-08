
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private String IF_KEYWORD = "\\bif\\b";
    private String ELSE_IF_KEYWORD = "\\belse if\\b";
    private String ELSE_KEYWORD = "\\belse\\b";
    private String INT_KEYWORD = "\\bint\\b";
    private String BOOLEAN_KEYWORD = "\\bboolean\\b";
    private String CHAR_KEYWORD = "\\bchar\\b";
    private String BOOLEAN_LIT = "\\b(true|false)\\b";
    private String COMPARISON_OP = ">=|<=|==|!=|>|<";
    private String ARITHMETIC_OP = "[+\\-*/]";
    private String ASSIGNMENT_OP = "=";
    private String IDENT = "[a-zA-Z$_][a-zA-Z0-9$_]*";
    private String INTEGER_LIT = "[0-9]+";
    private String OPEN_PAREN = "\\(";
    private String CLOSE_PAREN = "\\)";
    private String OPEN_BRACKET = "\\{";
    private String CLOSE_BRACKET = "\\}";

    private String[] tokens = {
        "IF_KEYWORD",
        "ELSE_IF_KEYWORD",
        "ELSE_KEYWORD",
        "INT_KEYWORD",
        "BOOLEAN_KEYWORD",
        "CHAR_KEYWORD",
        "BOOLEAN_LIT",
        "COMPARISON_OP",
        "ARITHMETIC_OP",
        "ASSIGNMENT_OP",
        "IDENT",
        "INTEGER_LIT",
        "OPEN_PAREN",
        "CLOSE_PAREN",
        "OPEN_BRACKET",
        "CLOSE_BRACKET"};

    private String[] tokenPatterns = {
        IF_KEYWORD,
        ELSE_IF_KEYWORD,
        ELSE_KEYWORD,
        INT_KEYWORD,
        BOOLEAN_KEYWORD,
        CHAR_KEYWORD,
        BOOLEAN_LIT,
        COMPARISON_OP,
        ARITHMETIC_OP,
        ASSIGNMENT_OP,
        IDENT,
        INTEGER_LIT,
        OPEN_PAREN,
        CLOSE_PAREN,
        OPEN_BRACKET,
        CLOSE_BRACKET
    };

    public String getToken(String lex, int line) {
        String token = "UNDEFINED";

        for (int i = 0; i < tokenPatterns.length; i++) {
            Pattern pattern = Pattern.compile(tokenPatterns[i]);
            Matcher matcher = pattern.matcher(lex);

            if (matcher.find()) {
                token = tokens[i];
                System.out.println(line + token);
                return token;
            }
        }

        return token;
    }
}
