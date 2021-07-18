package lab7.Tokens;

import lab7.Position;

public class TokenEndFragment extends Token {

    public TokenEndFragment(Position start, Position end) {
        super(start, end);
    }

    public String getTagName() {
        return "END";
    }

    @Override
    public String toString() {
        return "END" + this.startPosition + "-" + this.endPosition + ": " + '$';
    }
}
