
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Please type the if else then statement to the console (type \\q to exit):");
        Scanner sc = new Scanner(System.in);
        ArrayList<String> inputLines = new ArrayList<String>();
        String inputLine = "";

        while(inputLine.equals("\\q")){
            inputLine = sc.nextLine();
            inputLines.add(inputLine);
        }

        sc.close();

        // LexicalAnalyzer la = new LexicalAnalyzer();
        // String[] lexemes = {"if", "{", "x", "}"};        

        // for (String l: lexemes){
        //     la.getToken(l);
        // }
    }
}
