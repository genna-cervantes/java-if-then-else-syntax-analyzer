import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

class CodeBlock {
    public HashMap<Integer, String> statementMap = new HashMap<>();
    // String[] s = statementMap.values().toArray(new String[0]);

    public CodeBlock(HashMap<Integer, String> statementMap){
        this.statementMap = statementMap;
    }

    public void print(){
        System.out.println(statementMap);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (String s: statementMap.values()){
            sb.append(s);
        }

        return sb.toString();
    }

    public String[] getValues(){
        return statementMap.values().toArray(new String[0]);
    }

    public int getLength(){
        return statementMap.size();
    }

    public HashMap<Integer, String> getHashMap(){
        return this.statementMap;
    }
}

class ExpressionBlock {
    public HashMap<Integer, String> statementMap = new HashMap<>();

    public ExpressionBlock(HashMap<Integer, String> statementMap){
        this.statementMap = statementMap;
    }

    public void print(){
        System.out.println(statementMap);
    }
}

class Test {

    private static int codeBlockCount = 0;

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

        ArrayList<CodeBlock> codeBlocks = new ArrayList<>(); 
        HashMap<Integer, String> codeBlockStatementMap = new HashMap<>();            
        
        
        // 3 - 0 1 2 
        String universalStatement = "";
        int localLineCount = 0;

        for (int i = 0; i <= (lineCount + codeBlockCount) + 1; i++){
            String statement;

            
            if (!universalStatement.equals("")){
                statement = universalStatement;
                universalStatement = "";
            }else{
                statement = statementMap.get(localLineCount);
                localLineCount++;
            }

            // check string is not empty
            if (statement instanceof String){
    
                if (statement.indexOf('}') == -1){
                    codeBlockStatementMap.put(localLineCount, statement);
                    continue;
                }
    
                if (statement.indexOf('}') != -1){
                    int index = statement.indexOf('}') + 1;
                    String left = statement.substring(0, index);
                    String right = statement.substring(index);
    
                    codeBlockStatementMap.put(localLineCount, left);
                    universalStatement = right;
                    i++;
                    
                    codeBlocks.add(new CodeBlock(new HashMap<>(codeBlockStatementMap)));
                    codeBlockStatementMap.clear();
    
                }
            }

        }

        // for (CodeBlock cb: codeBlocks){
        //     cb.print();
        // }

        // throw error tapos try catch ko nlng lahat
        ValidationResult checkIfResult = checkIf(codeBlocks.get(0));
        if (!checkIfResult.getStatus()){
            String errStr = String.format("Error: %s at line %d", checkIfResult.getMessage(), checkIfResult.getLine());
            System.out.println(errStr);
            return;    
        }



        // test number of brackets first before splitting kasi nakabase sa brackets ung splitting
        // String[] codeBlocks = sb.toString().split("(?<=})");

        // if (checkIf(codeBlocks[0])){
        //     System.out.println("right syntax");
        // }else{
        //     System.out.println("wrong syntax");
        // }


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
                    codeBlockCount++;
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

    private static ValidationResult checkParenthesis(CodeBlock cb){
 
        HashMap<Integer, String> statementMap = cb.getHashMap();
        int lineCount = cb.getLength();

        int counter = 0;
        for (int i = 0; i < lineCount; i++){
            String statement = statementMap.get(i + 1);

            // checks for empty lines in input
            if (statement == null){
                continue;
            }

            char[] statementCharArr = statement.toCharArray();

            for (int j = 0; j < statementCharArr.length; j++) {
                char s = statementCharArr[j];
                if (s == '(') {
                    counter++;
                }

                if (s == ')') {
                    // Check for adjacent parentheses without content
                    if (j > 0 && statementCharArr[j - 1] == '(') {
                        // If adjacent parentheses are found and there are more characters between them
                        if (counter == 1) { // Ensure they are not inside another pair
                            return new ValidationResult(
                                "Incorrect parenthesis use - adjacent parentheses found", i + 1, false
                            );
                        }
                    }

                    counter--; // Decrease parenthesis counter
                }
            }
        }

        if (counter != 0){
            ValidationResult valRes = new ValidationResult("incorrect parenthesis use - missing partner parenthesis", lineCount - 1, false);
            return valRes;
        }
        return new ValidationResult(counter == 0);
    }

    
    private static ArrayList<ExpressionBlock> branchExpression(CodeBlock cb){

        ArrayList<ExpressionBlock> expressionBlocks = new ArrayList<>();

        int lineCount = cb.getLength();
        HashMap<Integer, String> statementMap = cb.getHashMap();

        HashMap<Integer, String> expressionStatementMap = new HashMap<>();
        
        int localLineCount = 0;
        String universalStatement = "";

        for (int i = 0; i <= (lineCount + codeBlockCount) + 1; i++){
            String statement;

            if (!universalStatement.equals("")){
                statement = universalStatement;
                universalStatement = "";
            }else{
                statement = statementMap.get(localLineCount);
                localLineCount++;
            }

            // check string is not empty
            if (statement instanceof String && !statement.equals("")){
    
                if (statement.indexOf(')') == -1){
                    expressionStatementMap.put(localLineCount, statement);
                    continue;
                }
    
                if (statement.indexOf(')') != -1){
                    int index = statement.indexOf(')') + 1;
                    String left = statement.substring(0, index);
                    String right = statement.substring(index);
    
                    expressionStatementMap.put(localLineCount, left);
                    universalStatement = right;
                    i++;
                    
                    expressionBlocks.add(new ExpressionBlock(new HashMap<>(expressionStatementMap)));
                    expressionStatementMap.clear();
    
                }
            }

        }

        return expressionBlocks;
        
    }

    private static ValidationResult checkIf(CodeBlock cb) {

        ValidationResult checkParenthesisResult = checkParenthesis(cb);

        if (!checkParenthesisResult.getStatus()){
            System.out.println(checkParenthesisResult.getLine());
            System.out.println(checkParenthesisResult.getMessage());
        }else{

            // checks if the if code block starts with if ()
            for (int i = 0; i < cb.getLength(); i++){
                String[] lines = cb.getValues();
                String s = lines[i];

                if(!s.trim().isEmpty()){
                    
                    if (!s.trim().matches("^if\\s*\\(.*$")){
                        ValidationResult valRes = new ValidationResult("illegal start of if statement", i + 1, false);
                        return valRes;
                    }else{
                        // tama lng ung start

                        // palitan (?) or gawa bago codeblock na wala na unf if ( - sa simula para ung (()()) expressions nlng ung ibbrnach out
                        break;
                    }
                }
            }

            ArrayList<ExpressionBlock> expressionBlocks = branchExpression(cb);
            
            for (ExpressionBlock eb: expressionBlocks){
                eb.print();
            }
        }

        // return success;
        ValidationResult valRes = new ValidationResult(true);
        return valRes;
    }
}
