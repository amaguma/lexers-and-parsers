package lab3;

public class Token {

    private int lineNumber, columnNumber;
    private String lexeme, tagName;

    Token(String tagName, int lineNumber, int columnNumber, String lexeme) {
        this.tagName = tagName;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return tagName + " (" + lineNumber + ", " + columnNumber + "): " + lexeme;
    }
}

