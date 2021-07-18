package lab4.Tokens;

import lab4.Position;

public abstract class Token {
    protected final Position startPosition, endPosition;

    Token(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

}
