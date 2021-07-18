"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Position = void 0;
class Position {
    constructor(line, column) {
        this.line = line;
        this.column = column;
    }
    getLine() {
        return this.line;
    }
    getColumn() {
        return this.column;
    }
    setLine(lineNumber) {
        this.line = lineNumber;
    }
    setNextLine() {
        this.line++;
    }
    setColumn(columnNumber) {
        this.column = columnNumber;
    }
    setNextColumn() {
        this.column++;
    }
    toString() {
        return '(' + this.line + ',' + this.column + ')';
    }
}
exports.Position = Position;
