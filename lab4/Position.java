package lab4;

public class Position {
    private int line, column;

    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public Position(Position currentPos) {
        this.line  = currentPos.getLine();
        this.column   = currentPos.getColumn();
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int lineNumber) {
        this.line = lineNumber;
    }

    public void setNextLine() {
        this.line++;
    }

    public int getColumn() {
        return column;
    }

    public void setNextColumn() {
        this.column++;
    }

    public void setColumn(int columnNumber) {
        this.column = columnNumber;
    }

    @Override
    public String toString() {
        return "(" + line + "," + column + ")";
    }

}