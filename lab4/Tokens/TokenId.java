package lab4.Tokens;

import lab4.Position;

public class TokenId extends Token{
    private String lexeme;

    public TokenId(Position startPosition, Position endPosition, String lexeme) {
        super(startPosition, endPosition);
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "IDENTIFIER" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}
