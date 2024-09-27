import java.util.ArrayList;
import java.util.Scanner;

class Test {
    public static void main(String[] args) {
        
        ArrayList<String> inputArr = new ArrayList<>();
        String inputStr = new String();

        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the if-then-else statement: (input \\q to exit)");
        while (!inputStr.equals("\\q")) {
            inputStr = sc.nextLine();
            inputArr.add(inputStr);
        }

        sc.close();

        for (String s : inputArr){
            System.out.println(s);
        }

    }
}