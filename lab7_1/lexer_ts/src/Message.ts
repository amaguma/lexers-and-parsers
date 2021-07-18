import { Position } from "./Position";

class Message {


    private endPosition: Position;
    private message: string;

    constructor(endPosition: Position, message: string) {
        this.endPosition = endPosition;
        this.message = message;
    }

    public toString(): string {
        return 'ERROR ' + this.endPosition.toString() + ': ' + this.message;
    }
}

export { Message }
