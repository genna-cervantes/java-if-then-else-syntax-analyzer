import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ValidationResult {
    private String message;
    private int line;
    private boolean status; // true is no error false is error

    public ValidationResult(String message, int line, boolean status){
        this.message = message;
        this.line = line;
        this.status = status;
    }

    public ValidationResult(boolean status){
        this.status = status;
    }

    public boolean getStatus(){
        return this.status;
    }

    public String getMessage(){
        return this.message;
    }

    public int getLine(){
        return this.line;
    }
}

class Test {

    public static void main(String[] args) {

        HashMap<Integer, String> statementMap = new HashMap<>();
        StringBuilder sb = new StringBuilder("");
        String inputStr = new String();

        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the if-then-else statement: (input \\q to exit)");

        int lineCount = 0;
        while (!inputStr.equals("\\q")) {
            inputStr = sc.nextLine();
            statementMap.put(lineCount, inputStr);
            lineCount++;
            // sb.append(inputStr);
        }

        sc.close();

        ValidationResult checkBracketsResult = checkBrackets(statementMap, lineCount);
        if (!checkBracketsResult.getStatus()) {
            String errStr = String.format("Error: %s at line %d", checkBracketsResult.getMessage(), checkBracketsResult.getLine());
            System.out.println(errStr);
            return;
        }

        // test number of brackets first before splitting
        String[] codeBlocks = sb.toString().split("(?<=})");

        if (checkIf(codeBlocks[0])){
            System.out.println("right syntax");
        }else{
            System.out.println("wrong syntax");
        }


        // for (String s: codeBlocks){
        //     System.out.println(s);
        // }
    }

    private static ValidationResult checkBrackets(HashMap<Integer, String> statementMap, int lineCount) {

        int counter = 0;
        for (int i = 0; i < lineCount; i++){
            String statement = statementMap.get(i);

            char[] statementCharArr = statement.toCharArray();

            for (char s : statementCharArr) {
                if (s == '{') {
                    counter++;
                }
                if (s == '}') {
                    counter--;
                    if (counter < 0) {
                        ValidationResult valRes = new ValidationResult("incorrect bracket use - missing partner bracket", lineCount - 1, false);
                        return valRes;
                    }
                }
            }
        }

        if (counter != 0){
            ValidationResult valRes = new ValidationResult("incorrect bracket use - missing partner bracket", lineCount - 1, false);
            return valRes;
        }
        return new ValidationResult(counter == 0);
    }

    private static boolean checkParenthesis(String statement){
        
        StringBuilder extractedParenthesis = new StringBuilder();
        Pattern pattern = Pattern.compile("[()]"); // Regex to match '(' or ')'
        Matcher matcher = pattern.matcher(statement);
        
        while (matcher.find()) {
            extractedParenthesis.append(matcher.group()); // Append found parentheses to the result
        }

        // adjacent parenthesis check
        if (extractedParenthesis.charAt(extractedParenthesis.length() - 1) == ')'){
            if (extractedParenthesis.charAt(extractedParenthesis.length() - 2) == '('){
                if (extractedParenthesis.length() != 2){
                    return false;
                }
            }
        }

        int counter = 0;
        for (int i = 0; i < extractedParenthesis.length(); i++){

            if (extractedParenthesis.charAt(i) == '('){
                counter++;
            }

            if (extractedParenthesis.charAt(i) == ')'){
                counter--;
                if (counter < 0){
                    return false;
                }
            }

        }

        return counter == 0;
    }

    private static boolean checkBooleanExpression(String ifBlockTrimmed){
        
        int startIndex = ifBlockTrimmed.indexOf('(') + 1;
        int endIndex = ifBlockTrimmed.lastIndexOf(')');
        String booleanExpression = ifBlockTrimmed.substring(startIndex, endIndex);

        System.out.println(booleanExpression);

        // logical expressions ( && || ! )
        // relational expressins ( == <= )
        // boolean variables ( bool )
        // methods returning bool ( function() )
        // instanceof operator ( obj instanceof Obj ) -- solo lang
        // combination of expressions


        return true;

    }

    private static boolean checkIf(String ifBlock) {

        System.out.println(ifBlock);

        // ung loob basta nag eend sa ;
        
        // if if ba ung starting
        String ifBlockTrimmed = ifBlock.trim();        
        if (!ifBlockTrimmed.matches("if\\s*\\(.*")){
            return false;
        }
        
        // check ung expression ung number of parenthesis
        if (!checkParenthesis(ifBlockTrimmed)){
            return false;
        }
        
        // check ung loob nung expression if boolean ba
        
        if (!checkBooleanExpression(ifBlockTrimmed)){
            return false;
        }

        return true;
    }
}
