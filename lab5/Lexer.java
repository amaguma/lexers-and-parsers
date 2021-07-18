package lab5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

enum CharIndex {
    CHAR_C(0), CHAR_A(1), CHAR_S(2), CHAR_E(3), CHAR_B(4), CHAR_R(5), CHAR_K(6),
    CHAR_PLUS(7), CHAR_L_BRACKET(8), CHAR_ASTERISK(9), CHAR_R_BRACKET(10), CHAR_NUM(11),
    CHAR_SPACE(12), CHAR_NEWLINE(13), CHAR_LETTER(14), CHAR_ANY(15);
    private int code;
    CharIndex(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }
}

enum Tag {
    SPACE("SPACE"), IDENTIFIER("IDENTIFIER"), NUMBER_LITERAL("NUMBER_LITERAL"),
    KEY_WORD("KEY_WORD"), OP("OP"), COMMENT("COMMENT"), ERROR("ERROR");
    private String code;
    Tag(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}


public class Lexer {
    private final int[][] table;
    private String fragment;
    private ArrayList<Token> tokens;
    private Position startPosition;
    private Position endPosition;
    private int index;
    private int currentStateIndex;
    private ArrayList<Message> messages;

    private Lexer(String fragment) {
        this.table = new int[][] {
            /*  c   a   s   e   b   r   k   +     (   *     )    num   sp    NL     a-z
/*  START   */{ 1,  4,  4,  4,  5,  4,  4,  14,   8,  -2,  -2,   12,   13,   13,    4,  -1},
/*   ID-1   */{ 4,  2,  4,  4,  4,  4,  4, -1,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/*   ID-2   */{ 4,  4,  3,  4,  4,  4,  4, -1,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/*   ID-3   */{ 4,  4,  4, 16,  4,  4,  4, -1,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/*   ID-4   */{ 4,  4,  4,  4,  4,  4,  4, -1,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/*   ID-5   */{ 4,  4,  4,  4,  4, 17,  4, -1,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/*   ID-6   */{ 4,  7,  4,  4,  4,  4,  4, -1,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/*   ID-7   */{ 4,  4,  4,  4,  4,  4, 16, -1,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/*  COM-8   */{-2, -2, -2, -2, -2, -2, -2, -2,   -2,   9,  -2,   -2,   -2,   -2,   -2,  -2},
/*  COM-9   */{ 9,  9,  9,  9,  9,  9,  9,  9,    9,  10,   9,    9,    9,    9,    9,   9},
/* COM-10   */{ 9,  9,  9,  9,  9,  9,  9,  9,    9,  10,  11,    9,    9,    9,    9,   9},
/* COM-11   */{-1, -1, -1, -1, -1, -1, -1,  -1,  -1,  -1,  -1,   -1,   -1,   -1,   -1,  -1},
/* NUM-12   */{-2, -2, -2, -2, -2, -2, -2,  -1,  -2,  -2,  -2,   12,   -1,   -1,   -2,  -1},
/*  SP-13   */{-1, -1, -1, -1, -1, -1, -1,  -1,  -1,  -1,  -1,   -1,   13,   13,   -1,  -1},
/*  OP-14   */{-1, -1, -1, -1, -1, -1, -1,  15,  -1,  -1,  -1,   -1,   -1,   -1,   -1,  -1},
/*  OP-15   */{-1, -1, -1, -1, -1, -1, -1,  -1,  -1,  -1,  -1,   -1,   -1,   -1,   -1,  -1},
/* KEY-16   */{ 4,  4,  4,  4,  4,  4,  4,  4,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
/* ID-17    */{ 4,  4,  4,  6,  4,  4,  4,  4,   -1,  -1,  -1,    4,   -1,   -1,    4,  -1},
//              0   1   2   3   4   5   6    7    8    9   10    11    12    13    14   15
        };
        this.fragment = fragment;
        this.tokens = new ArrayList<>();
        this.startPosition = new Position(1, 1);
        this.endPosition = new Position(1, 1);
        this.index = 0;
        this.currentStateIndex = 0;
        this.messages = new ArrayList<>();
    }

    private int getCharCode(char c) {
        switch (c) {
            case 'c':
                return CharIndex.CHAR_C.getCode();
            case 'a':
                return CharIndex.CHAR_A.getCode();
            case 's':
                return CharIndex.CHAR_S.getCode();
            case 'e':
                return CharIndex.CHAR_E.getCode();
            case 'b':
                return CharIndex.CHAR_B.getCode();
            case 'r':
                return CharIndex.CHAR_R.getCode();
            case 'k':
                return CharIndex.CHAR_K.getCode();
            case '+':
                return CharIndex.CHAR_PLUS.getCode();
            case '(':
                return CharIndex.CHAR_L_BRACKET.getCode();
            case '*':
                return CharIndex.CHAR_ASTERISK.getCode();
            case ')':
                return CharIndex.CHAR_R_BRACKET.getCode();
            case ' ':
                return CharIndex.CHAR_SPACE.getCode();
            case '\n':
                return CharIndex.CHAR_NEWLINE.getCode();
        }
        if (c >= '0' && c <= '9') {
            return CharIndex.CHAR_NUM.getCode();
        }
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return CharIndex.CHAR_LETTER.getCode();
        }
        return CharIndex.CHAR_ANY.getCode();
    }

    private String getTagName(int state) {
        if ((state >= 1 && state <= 7) || state == 17){
            return Tag.IDENTIFIER.getCode();
        }
        if (state == 14 || state == 15) {
            return Tag.OP.getCode();
        }
        switch (state) {
            case 11:
                return Tag.COMMENT.getCode();
            case 12:
                return Tag.NUMBER_LITERAL.getCode();
            case 13:
                return Tag.SPACE.getCode();
            case 16:
                return Tag.KEY_WORD.getCode();
            default:
                return Tag.ERROR.getCode();
        }
    }

    private boolean isEnd() {
        return index > fragment.length() - 1;
    }

    private void app(){
        while (!this.isEnd()) {
            StringBuilder word = new StringBuilder();
            currentStateIndex = 0;
            startPosition.setLine(endPosition.getLine());
            startPosition.setColumn(endPosition.getColumn());
            boolean endToken = false;
            boolean error = false;
            while (true) {
                char currentChar = fragment.charAt(this.index);
                int charIndex = getCharCode(currentChar);
                int nextStateIndex = table[currentStateIndex][charIndex];
                if (nextStateIndex == -1) {
                    endToken = true;
                    break;
                }
                if (nextStateIndex == -2) {
                    if (currentChar == '\n') {
                        if (currentStateIndex != 8) {
                            endPosition.setNextLine();
                            endPosition.setColumn(1);
                        }
                    } else {
                        word.append(currentChar);
                        endPosition.setNextColumn();
                        index++;
                    }
                    error = true;
                    break;
                }
                currentStateIndex = nextStateIndex;
                word.append(currentChar);
                if (currentChar == '\n') {
                    endPosition.setNextLine();
                    endPosition.setColumn(1);
                } else {
                    endPosition.setNextColumn();
                }
                index++;
                if (this.isEnd()) {
                    endToken = true;
                    break;
                }
            }
            if (endToken) {
                Token newToken = new Token(getTagName(currentStateIndex), new Position(this.startPosition), new Position(this.endPosition), word.toString().replaceAll("\n", " "));
                tokens.add(newToken);
            }
            if (error) {
                Token newToken = new Token(getTagName(-2), new Position(this.startPosition), new Position(this.endPosition), word.toString().replaceAll("\n", " "));
                tokens.add(newToken);
                messages.add(new Message(new Position(this.endPosition), "Unexpected characters"));
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