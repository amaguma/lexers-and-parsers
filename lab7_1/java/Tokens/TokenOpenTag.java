package lab7.Tokens;

import lab7.Position;

public class TokenOpenTag extends Token {

    public TokenOpenTag(Position start, Position end) {
        super(start, end);
    }

    public String getTagName() {
        return "OPEN_TAG";
    }

    @Override
    public String toString() {
        return "OPEN_TAG" + this.startPosition + "-" + this.endPosition + ": " + '<';
    }
}
