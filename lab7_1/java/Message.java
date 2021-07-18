package lab7;

public class Message {


    private final Position endPosition;
    private final String message;

    public Message(Position endPosition, String message) {
        this.endPosition = endPosition;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ERROR " + this.endPosition + ": " + this.message;
    }
}

