package lab7.Tokens;

import lab7.Position;

public abstract class Token {
    protected final Position startPosition, endPosition;

    Token(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public abstract String getTagName();
}
