"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Message = void 0;
class Message {
    constructor(endPosition, message) {
        this.endPosition = endPosition;
        this.message = message;
    }
    toString() {
        return 'ERROR ' + this.endPosition.toString() + ': ' + this.message;
    }
}
exports.Message = Message;
