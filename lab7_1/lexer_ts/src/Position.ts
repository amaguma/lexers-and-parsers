class Position {
    private line: number
    private column: number;

    constructor(line: number, column: number) {
        this.line = line;
        this.column = column;
    }

    public getLine(): number {
        return this.line;
    }

    public getColumn(): number {
        return this.column
    }

    public setLine(lineNumber: number): void {
        this.line = lineNumber;
    }

    public setNextLine(): void {
        this.line++;
    }

    public setColumn(columnNumber: number): void {
        this.column = columnNumber;
    }

    public setNextColumn(): void {
        this.column++;
    } 

    public toString(): string {
        return '(' + this.line + ',' + this.column + ')'; 
    }

}

export { Position }