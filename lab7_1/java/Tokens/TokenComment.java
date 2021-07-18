package lab7.Tokens;

import lab7.Position;

public class TokenComment extends Token {
    private String lexeme;

    public TokenComment(Position start, Position end, String  lexeme) {
        super(start, end);
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getTagName() {
        return "COMMENT";
    }

    @Override
    public String toString() {
        return "COMMENT" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}
