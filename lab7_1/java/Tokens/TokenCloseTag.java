package lab7.Tokens;

import lab7.Position;

public class TokenCloseTag extends Token {


    public TokenCloseTag(Position start, Position end) {
        super(start, end);
    }

    public String getTagName() {
        return "CLOSE_TAG";
    }

    @Override
    public String toString() {
        return "CLOSE_TAG" + this.startPosition + "-" + this.endPosition + ": " + '>';
    }
}
