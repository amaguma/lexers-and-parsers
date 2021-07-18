"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Token = void 0;
class Token {
    constructor(tagName, startPosition, endPosition, lexeme) {
        this.tagName = tagName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lexeme = lexeme;
    }
    getTag() {
        return this.tagName;
    }
    toString() {
        return this.tagName + this.startPosition.toString() + '-' + this.endPosition.toString() + ': ' + this.lexeme;
    }
}
exports.Token = Token;
