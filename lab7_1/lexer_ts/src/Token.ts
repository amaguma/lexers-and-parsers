import { Position } from "./Position"

class Token {

    private startPosition: Position;
    private endPosition: Position;
    private lexeme: string
    private tagName: string;

    constructor(tagName: string, startPosition: Position, endPosition: Position, lexeme: string) {
        this.tagName = tagName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lexeme = lexeme;
    }

    public getTag(): string {
        return this.tagName;
    }

    public toString(): string {
        return this.tagName  + this.startPosition.toString() + '-' + this.endPosition.toString() + ': ' + this.lexeme;
    }
}


export { Token }