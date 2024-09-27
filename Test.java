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

        String statement = sb.toString();

        if(checkBrackets(statement)){
            System.out.println("right bracket count");
        }else{
            System.out.println("wrong bracket count");
        }

        // test number of brackets first before splitting
        // String[] codeBlocks = sb.toString().split("(?<=})");

        // for (String s: codeBlocks){
        //     System.out.println(s);
        // }

        // if(checkIf(codeBlocks[0])){
        //     System.out.println("succcesss");
        // }

    }

    private static boolean checkBrackets(String statement){
        
        int counter = 0;

        char[] statementCharArr = statement.toCharArray();

        for (char s: statementCharArr){
            if (s == '{'){
                counter++;
            }
            if (s == '}'){
                counter--;
                if (counter < 0){
                    return false;
                }
            }
        }

        return counter == 0;
    }

    private static boolean checkIf(String ifBlock){
        return true;
    }
}