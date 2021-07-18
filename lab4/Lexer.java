package lab4;

import lab4.Tokens.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Lexer {
    private String fragment;
    private ArrayList<Token> tokens;
    private Position startPosition;
    private Position endPosition;
    private int index;
    private ArrayList<Message> messages;

    private Lexer(String fragment) {
        this.fragment = fragment;
        this.tokens = new ArrayList<>();
        this.startPosition = new Position(1, 1);
        this.endPosition = new Position(1, 1);
        this.index = 0;
        this.messages = new ArrayList<>();
    }

    private boolean isKeyWord(String lexeme) {
        return lexeme.equals("mov") || lexeme.equals("eax");
    }

    private boolean isIdentifier(String lexeme) {
        char firstSymbol = lexeme.charAt(0);
        return ((firstSymbol >= 'a' && firstSymbol <= 'z') || (firstSymbol >= 'A' && firstSymbol <= 'Z'));
    }

    private boolean isNumberLiteral(String lexeme) {
        char firstSymbol = lexeme.charAt(0);
        if (firstSymbol >= '0' && firstSymbol <= '9') {
            if (lexeme.charAt(lexeme.length() - 1) == 'h') {
                for(int i = 1; i < lexeme.length() - 1; i++) {
                    char symbol = lexeme.charAt(i);
                    if (!(symbol >= '0' && symbol <= '9') && !(symbol >= 'a' && symbol <= 'f') && !(symbol >= 'A' && symbol <= 'F')) {
                        return false;
                    }
                }
            } else {
                for(int i = 1; i < lexeme.length(); i++) {
                    char symbol = lexeme.charAt(i);
                    if (!(symbol >= '0' && symbol <= '9')) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean isEnd() {
        return index > fragment.length() - 1;
    }

    private void app(){
        while (!this.isEnd()) {
            StringBuilder word = new StringBuilder();
            startPosition.setLine(endPosition.getLine());
            startPosition.setColumn(endPosition.getColumn());
            while (true) {
                char currentChar = fragment.charAt(this.index);
                String lexeme;
                if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
                    lexeme = word.toString();
                    if (lexeme.length() != 0) {
                        if (this.isKeyWord(lexeme)) {
                            Token newToken = new TokenKeyWord(new Position(this.startPosition), new Position(this.endPosition), lexeme);
                            tokens.add(newToken);
                        } else if (this.isIdentifier(lexeme)) {
                            Token newToken = new TokenId(new Position(this.startPosition), new Position(this.endPosition), lexeme);
                            tokens.add(newToken);
                        } else if (this.isNumberLiteral(lexeme)) {
                            Token newToken = new TokenNumber(new Position(this.startPosition), new Position(this.endPosition), lexeme);
                            tokens.add(newToken);
                        } else {
                            Token newToken = new TokenError(new Position(this.startPosition), new Position(this.endPosition), lexeme);
                            tokens.add(newToken);
                            messages.add(new Message(new Position(this.endPosition), "Unexpected characters"));
                        }
                        if (currentChar == '\n') {
                            endPosition.setNextLine();
                            endPosition.setColumn(1);
                        } else {
                            endPosition.setNextColumn();
                        }
                    } else {
                        if (currentChar == '\n') {
                            endPosition.setNextLine();
                            endPosition.setColumn(1);
                        } else {
                            endPosition.setNextColumn();
                        }
                    }
                    index++;
                    break;
                }
                if (!(currentChar >= 'a' && currentChar <= 'z') && !(currentChar >= 'A' && currentChar <= 'Z') &&
                        !(currentChar >= '0' && currentChar <= '9')) {
                    endPosition.setNextColumn();
                    index++;
                    Token newToken = new TokenError(new Position(this.startPosition), new Position(this.endPosition), word.toString() + currentChar);
                    tokens.add(newToken);
                    messages.add(new Message(new Position(endPosition), "Unexpected characters"));
                    break;
                }
                word.append(currentChar);
                index++;
                this.endPosition.setNextColumn();
                if (this.isEnd()) {
                    break;
                }
            }
        }
    }

    private ArrayList<Token> getTokens() {
        return this.tokens;
    }

    private boolean messagesIsEmpty() {
        return messages.isEmpty();
    }

    private ArrayList<Message> getMessages() {
        return this.messages;
    }

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

    public static void main(String[] args){
        String program = readFile("test.txt");
        Lexer lexer = new Lexer(program);
        lexer.app();
        for(Token token: lexer.getTokens()) {
            System.out.println(token);
        }
        if (!lexer.messagesIsEmpty()) {
            System.out.println("\nErrors");
            for (Message message: lexer.getMessages()) {
                System.out.println(message);
            }
        }
    }
}