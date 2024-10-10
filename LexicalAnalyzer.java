
public class LexicalAnalyzer {
    private String KEYWORDS = "\\b(if|else if|else|int|boolean|char)\\b";
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
}
