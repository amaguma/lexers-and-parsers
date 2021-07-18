package lab5;

public class Token {
    private final Position startPosition, endPosition;
    private final String tagName, lexeme;

    Token(String tagName, Position startPosition, Position endPosition, String lexeme) {
        this.tagName = tagName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return  this.tagName + " " + this.startPosition  + "-" + this.endPosition + ": " + this.lexeme;

    }
}
