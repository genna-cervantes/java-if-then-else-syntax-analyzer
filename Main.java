
import java.util.ArrayList;
import java.util.Scanner;

class Lexeme{
    public int line;
    public String lex;
    public int charCount;

    public Lexeme(int line, String lex){
        this.line = line;
        this.lex = lex;
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("Please type the if else then statement to the console (type \\q to exit):");
        Scanner sc = new Scanner(System.in);
        ArrayList<Lexeme> inputLexemes = new ArrayList<Lexeme>();
        String inputLine = "";
        int lineCounter = 0;

        while(!inputLine.equals("\\q")){
            inputLine = sc.nextLine();

            String[] splitStrings = inputLine.split("\\s+");

            for (String s: splitStrings){
                Lexeme lex = new Lexeme(lineCounter, s);
                inputLexemes.add(lex);
            }

            lineCounter++;
        }

        sc.close();

        LexicalAnalyzer la = new LexicalAnalyzer();

        for (int i = 0; i < inputLexemes.size(); i++){
            if (i >= inputLexemes.size() - 1){
                return;
            }

            Lexeme l = inputLexemes.get(i);

            la.getToken(l.lex, l.line);
        }
    }
}
