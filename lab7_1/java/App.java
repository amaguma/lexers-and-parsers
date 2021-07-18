package lab7;

import lab7.AST.NonTermNode;
import lab7.Tokens.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class App {

    private static String readFile(String fileName) {
        StringBuilder builder = new StringBuilder();
        String line;
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            while ((line = in.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public static void main(String[] args) {
        String program = readFile("test.txt");
//        String program = readFile("testLexer.txt");
//        String program = readFile("testParser.txt");
        Lexer lexer = new Lexer(program);
        lexer.app();
        System.out.println("Tokens:");
        for(Token token: lexer.getTokens()) {
            System.out.println(token);
        }
        if (!lexer.messagesIsEmpty()) {
            lexer.printMessages();
            return;
        }

        Parser parser = new Parser(lexer.getTokens());
        NonTermNode root = parser.parse();
        System.out.println("\nAST:");
        parser.printAST(root, 0);
        parser.printMessages();
    }
}
