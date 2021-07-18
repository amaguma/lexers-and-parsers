package lab7.Tokens;

import lab7.Position;

public class TokenTerminal extends Token {
    private String lexeme;

    public TokenTerminal(Position start, Position end, String  lexeme) {
        super(start, end);
        this.lexeme = lexeme;
    }

    public String getTagName() {
        return "TERMINAL";
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return "TERMINAL" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}
