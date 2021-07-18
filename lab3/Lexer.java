package lab3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


enum TagName {
    OPEN_TAG("OPEN_TAG"), CLOSE_TAG("CLOSE_TAG"), SYMBOL("SYMBOL"),
    SPACE("SPACE"), KEY_WORD("KEY_WORD");
    private String code;
    TagName(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}

public class Lexer {
    private final String fragment;
    private final Pattern p;
    private String line;
    private int lineNumber, columnNumber;
    private ArrayList<Token> tokens;

    private Lexer(String fragment) {
        this.fragment = fragment;
        final String pattern = "^((?<space>\\s+)|(?<openTag><[\\d\\w]+>)|(?<closeTag></[\\d\\w]+>)|(?<keyword>&(lt|gt|amp);)|(?<symbol>[^<>&]))";
        this.p = Pattern.compile(pattern);
        this.line = "";
        this.lineNumber = 0;
        this.columnNumber = 1;
        this.tokens = new ArrayList<>();
    }

    private String nextLine() {
        return this.line = fragment.split("\n")[lineNumber];
    }

    private void app(){
        while(lineNumber < fragment.split("\n").length) {
            this.line = nextLine();
            this.lineNumber++;
            this.columnNumber = 1;
            while (this.line.length() != 0) {
                String tagName = "Syntax error";
                Matcher m = this.p.matcher(this.line);
                if (m.find()) {
                    String tokenName;
                    if (m.group("openTag") != null) {
                        tagName = TagName.OPEN_TAG.getCode();
                        tokenName = "openTag";
                    } else if (m.group("closeTag") != null) {
                        tagName = TagName.CLOSE_TAG.getCode();
                        tokenName = "closeTag";
                    } else if (m.group("keyword") != null) {
                        tagName = TagName.KEY_WORD.getCode();
                        tokenName = "keyword";
                    } else if (m.group("space") != null) {
                        tagName = TagName.SPACE.getCode();
                        tokenName = "space";
                    } else {
                        tagName = TagName.SYMBOL.getCode();
                        tokenName = "symbol";
                    }
                    this.tokens.add(new Token(tagName, this.lineNumber,
                            m.start(tokenName) + this.columnNumber, m.group(tokenName)));
                    this.columnNumber += m.start(tokenName) + m.group(tokenName).length();
                    this.line = this.line.substring(m.start(tokenName) + m.group(tokenName).length());
                } else {
                    this.tokens.add(new Token(tagName, this.lineNumber,this.columnNumber, "" ));
                    while (this.line.length() != 0 && !this.p.matcher(this.line).find()) {
                        this.line = this.line.substring(1);
                        this.columnNumber++;
                    }
                }
            }
        }
    }


    private ArrayList<Token> getTokens() {
        return this.tokens;
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

    public static void main(String[] args) {
        String fragment = readFile("test.txt");
        Lexer lexer = new Lexer(fragment);
        lexer.app();
        for(Token token: lexer.getTokens()) {
            System.out.println(token);
        }
    }
}

