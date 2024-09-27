//import java.util.ArrayList;

import java.util.Scanner;

class Test {

    public static void main(String[] args) {

        //ArrayList<String> inputArr = new ArrayList<>();
        StringBuilder sb = new StringBuilder("");
        String inputStr = new String();

        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the if-then-else statement: (input \\q to exit)");
        while (!inputStr.equals("\\q")) {
            inputStr = sc.nextLine();
            // inputArr.add(inputStr);
            sb.append(inputStr);
        }

        sc.close();

        if (!checkBrackets(sb.toString())) {
            System.out.println("Syntax Error: Incorrect brackets");
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

    private static boolean checkBrackets(String statement) {

        int counter = 0;

        char[] statementCharArr = statement.toCharArray();

        for (char s : statementCharArr) {
            if (s == '{') {
                counter++;
            }
            if (s == '}') {
                counter--;
                if (counter < 0) {
                    return false;
                }
            }
        }

        return counter == 0;
    }

    private static boolean checkParenthesis(String statement){
        
        int counter = 0;
        int openCounter = 0;
        int closeCounter = 0;

        char[] statementCharArr = statement.toCharArray();

        for (char s : statementCharArr) {
            
            // handles check for one pair only 
            if (openCounter > 1 || closeCounter > 1){
                return false;
            }

            if (s == '(') {
                counter++;
                openCounter++;
            }
            if (s == ')') {
                counter--;
                closeCounter++;
                if (counter < 0) {
                    return false;
                }
            }
        }

        return counter == 0;
    }

    // private static boolean checkBooleanExpression()

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


        return true;
    }
}
