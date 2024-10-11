
import java.util.ArrayList;
import java.util.Scanner;

class Line {

    public int line;
    public String str;
    public int charCount;

    public Line(int line, String str) {
        this.line = line;
        this.str = str;
    }
}

class Token {
    
    public int line;
    public String token;
    public int charCount;

    public Token(int line, String token) {
        this.line = line;
        this.token = token;
    }
}

public class Main {

    public static void main(String[] args) {

        System.out.println("Please type the if else then statement to the console (type \\q to exit):");
        Scanner sc = new Scanner(System.in);
        
        ArrayList<Line> inputLines = new ArrayList<>();
        String inputLine = "";
        int lineCounter = 0;

        while (!inputLine.equals("\\q")) {
            inputLine = sc.nextLine();

            if (inputLine.equals("")) {
                lineCounter++;
                continue;
            }

            Line line = new Line(lineCounter, inputLine);

            inputLines.add(line);
            lineCounter++;
        }

        sc.close();

        LexicalAnalyzer la = new LexicalAnalyzer();
        ArrayList<Token> tokens = new ArrayList<>();

        for (Line l: inputLines){

            // ignore last line "\q"
            if (l.line >= lineCounter - 1){
                break;
            }

            ArrayList<String> tokensInLine = la.tokenizeString(l.str);

            for (String t : tokensInLine) {
                Token token = new Token(l.line, t);
                tokens.add(token);
            }
        }

        for (Token t : tokens) {
            System.out.println(t.token + t.line);
        }

        SyntaxAnalyzer sa = new SyntaxAnalyzer(tokens);
        sa.parseIfThenElse();

    }
}
