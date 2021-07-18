package lab4.Tokens;

import lab4.Position;

public class TokenNumber extends Token{
    private long lexeme;

    public TokenNumber(Position startPosition, Position endPosition, String lexeme) {
        super(startPosition, endPosition);
        if (lexeme.charAt(lexeme.length() - 1) == 'h') {
            lexeme = lexeme.substring(0, lexeme.length() - 1);
            this.lexeme = Long.parseLong(lexeme, 16);
        } else  {
            this.lexeme = Long.parseLong(lexeme);
        }

    }

    @Override
    public String toString() {
        return "NUMBER_LITERAL" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}
