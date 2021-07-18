package lab7.Tokens;

import lab7.Position;

public class TokenNonTerminal extends Token {
    private String lexeme;

    public TokenNonTerminal(Position start, Position end, String  lexeme) {
        super(start, end);
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getTagName() {
        return "NONTERMINAL";
    }

    @Override
    public String toString() {
        return "NONTERMINAL" + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
}
