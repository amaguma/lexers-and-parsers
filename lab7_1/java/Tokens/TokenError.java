package lab7.Tokens;

import lab7.Position;

public class TokenError extends Token {
    private String lexeme;

    public TokenError(Position start, Position end, String  lexeme) {
        super(start, end);
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getTagName() {
        return "ERROR";
    }

    @Override
    public String toString() {
        return "ERROR" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}
