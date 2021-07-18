package lab7.Tokens;

import lab7.Position;

public class TokenKeyWord extends Token {

    public TokenKeyWord(Position start, Position end) {
        super(start, end);
    }

    public String getTagName() {
        return "KEY_WORD";
    }

    @Override
    public String toString() {
        return "KEY_WORD" + this.startPosition + "-" + this.endPosition + ": " + "axiom";
    }
}
