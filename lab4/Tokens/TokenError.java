package lab4.Tokens;

import lab4.Position;

public class TokenError extends Token{
    private String lexeme;

    public TokenError(Position startPosition, Position endPosition, String lexeme) {
        super(startPosition, endPosition);
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "ERROR" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}