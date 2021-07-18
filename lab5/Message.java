package lab5;

public class Message {


    private final Position position;
    private final String message;

    public Message(Position position, String message) {
        this.position = position;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ERROR " + this.position + ": " + this.message;
    }
}
