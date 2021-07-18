package lab4.Tokens;

import lab4.Position;

public class TokenKeyWord extends Token{
    private String lexeme;

    public TokenKeyWord(Position startPosition, Position endPosition, String lexeme) {
        super(startPosition, endPosition);
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "KEY_WORD" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}