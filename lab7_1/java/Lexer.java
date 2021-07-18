package lab7;


import lab7.Tokens.*;

import java.util.ArrayList;

public class Lexer {
    private String fragment;
    private ArrayList<Token> tokens;
    private Position startPosition;
    private Position endPosition;
    private int index;
    private ArrayList<Message> messages;

    public Lexer(String fragment) {
        this.fragment = fragment;
        this.tokens = new ArrayList<>();
        this.startPosition = new Position(1, 1);
        this.endPosition = new Position(1, 1);
        this.index = 0;
        this.messages = new ArrayList<>();
    }

    private boolean isEnd() {
        return this.index > this.fragment.length() - 1;
    }

    private boolean isKeyWord(String lexeme) {
        return lexeme.equals("axiom");
    }

    private boolean isTerminal(String lexeme){
        if (lexeme.length() > 1) {
            for (int i = 0; i < lexeme.length(); i++) {
                char symbol = lexeme.charAt(i);
                if (!(symbol >= 'a' && symbol <= 'z') && !(symbol >= '0' && symbol <= '9')) {
                    return false;
                }
            }
        } else {
            char symbol = lexeme.charAt(0);
            return (symbol >= 'a' && symbol <= 'z') || (symbol >= '0' && symbol <= '9') || symbol == '(' || symbol == ')' || symbol == '+' || symbol == '*';
        }
        return true;
    }

    private boolean isNonTerminal(String lexeme) {
        if (lexeme.length() > 1) {
            if (lexeme.charAt(lexeme.length() - 1) == '\'') {
                for (int i = 0; i < lexeme.length() - 1; i++) {
                    char symbol = lexeme.charAt(i);
                    if (!(symbol >= 'A' && symbol <= 'Z')) {
                        return false;
                    }
                }
            } else {
                for (int i = 0; i < lexeme.length(); i++) {
                    char symbol = lexeme.charAt(i);
                    if (!(symbol >= 'A' && symbol <= 'Z')) {
                        return false;
                    }
                }
            }
        } else {
            return (lexeme.charAt(0) >= 'A' && lexeme.charAt(0) <= 'Z');
        }
        return true;
    }

    private void addToken(String lexeme) {
        if (this.isKeyWord(lexeme)) {
            Token newToken = new TokenKeyWord(new Position(this.startPosition), new Position(this.endPosition));
            this.tokens.add(newToken);
        } else if (this.isTerminal(lexeme)) {
            Token newToken = new TokenTerminal(new Position(this.startPosition), new Position(this.endPosition), lexeme);
            this.tokens.add(newToken);
        } else if (this.isNonTerminal(lexeme)) {
            Token newToken = new TokenNonTerminal(new Position(this.startPosition), new Position(this.endPosition), lexeme);
            this.tokens.add(newToken);
        } else {
            Token newToken = new TokenError(new Position(this.startPosition), new Position(this.endPosition), lexeme);
            this.tokens.add(newToken);
            this.messages.add(new Message(new Position(this.endPosition), "Unexpected characters"));
        }
    }

    public void app() {
        while (!this.isEnd()) {
            StringBuilder word = new StringBuilder();
            this.startPosition.setLine(this.endPosition.getLine());
            this.startPosition.setColumn(this.endPosition.getColumn());
            while (true) {
                char currentChar = this.fragment.charAt(this.index);
                String lexeme;
                if (currentChar == '\'' && ((this.index == 0) || !this.isNonTerminal(Character.toString(fragment.charAt(this.index - 1))))) {
                    while (this.fragment.charAt(this.index) != '\n') {
                        char symbol = this.fragment.charAt(this.index);
                        word.append(symbol);
                        this.index++;
                        this.endPosition.setNextColumn();
                    }
                    Token newToken = new TokenComment(new Position(this.startPosition), new Position(this.endPosition), word.toString());
                    this.tokens.add(newToken);
                    this.index++;
                    this.endPosition.setNextLine();
                    break;
                } else if (currentChar == '<' || currentChar == '>') {
                    lexeme = word.toString();
                    Token newToken;
                    if (lexeme.length() != 0) {
                        this.addToken(lexeme);
                    }
                    this.startPosition.setLine(this.endPosition.getLine());
                    this.startPosition.setColumn(this.endPosition.getColumn());
                    this.endPosition.setNextColumn();
                    if (currentChar == '<') {
                        newToken = new TokenOpenTag(new Position(this.startPosition), new Position(this.endPosition));
                    } else {
                        newToken = new TokenCloseTag(new Position(this.startPosition), new Position(this.endPosition));
                    }
                    this.tokens.add(newToken);
                    this.index++;
                    break;
                } else if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
                    lexeme = word.toString();
                    if (lexeme.length() != 0) {
                        this.addToken(lexeme);
                        if (currentChar == '\n') {
                            this.endPosition.setNextLine();
                        } else {
                            this.endPosition.setNextColumn();
                        }
                    } else {
                        if (currentChar == '\n') {
                            this.endPosition.setNextLine();
                        } else {
                            this.endPosition.setNextColumn();
                        }
                    }
                    this.index++;
                    break;
                } else if (!(currentChar >= 'a' && currentChar <= 'z') && !(currentChar >= 'A' && currentChar <= 'Z') &&
                        !(currentChar >= '0' && currentChar <= '9') && currentChar != '\'' && currentChar != '('
                        && currentChar != ')' && currentChar != '+' && currentChar != '*') {
                    this.endPosition.setNextColumn();
                    this.index++;
                    Token newToken = new TokenError(new Position(this.startPosition), new Position(this.endPosition), word.toString() + currentChar);
                    this.tokens.add(newToken);
                    this.messages.add(new Message(new Position(this.endPosition), "Unexpected characters"));
                    break;
                }
                word.append(currentChar);
                this.index++;
                this.endPosition.setNextColumn();
                if (this.isEnd()) {
                    break;
                }
            }
        }
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    public boolean messagesIsEmpty() {
        return messages.isEmpty();
    }


    public void printMessages() {
        System.out.println("\nLEXICAL ERRORS");
        for (Message message : this.messages) {
            System.out.println(message);
        }
    }

}

