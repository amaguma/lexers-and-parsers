import { Position } from "./Position";
import { Token } from "./Token"
import { Message } from "./Message"
import { readFileSync } from 'fs';



enum TagName {
    OPEN_TAG = "OPEN_TAG", 
    CLOSE_TAG = "CLOSE_TAG", 
    TERMINAL = "TERMINAL",
    KEY_WORD = "KEY_WORD",
    NONTERMINAL = "NONTERMINAL",
    COMMENT = "COMMENT",
    ERROR = "ERROR"
}

class Lexer {
    private fragment: string;
    private index: number;
    private startPosition: Position;
    private endPosition: Position;
    private tokens: Array<Token>;
    private messages: Array<Message>

    constructor(fragment: string) {
        this.fragment = fragment;
        this.tokens = new Array();
        this.messages = new Array();
        this.startPosition = new Position(1, 1);
        this.endPosition = new Position(1, 1);
        this.index = 0;
    }

    private isEnd(): boolean {
        return this.index > this.fragment.length - 1;
    }

    private isKeyWord(lexeme: string): boolean {
        return lexeme === 'axiom';
    }

    private isTerminal(lexeme: string): boolean {
        if (lexeme.length > 1) {
            for (let i = 0; i < lexeme.length; i++) {
                const symbol = lexeme.charAt(i);
                if (!(symbol >= 'a' && symbol <= 'z') && !(symbol >= '0' && symbol <= '9')) {
                    return false;
                }
            }
        } else {
            const symbol = lexeme.charAt(0);
            if (!(symbol >= 'a' && symbol <= 'z') && !(symbol >= '0' && symbol <= '9') && symbol !== '(' && symbol !== ')' && symbol !== '+' && symbol !== '*') {
                return false;
            }
        }
        return true;
    }

    private isNonTerminal(lexeme: string): boolean {
        if (lexeme.length > 1) {
            if (lexeme.charAt(lexeme.length - 1) === '\'') {
                for (let i = 0; i < lexeme.length - 1; i++) {
                    const symbol = lexeme.charAt(i);
                    if (!(symbol >= 'A' && symbol <= 'Z')) {
                        return false;
                    }
                }
            } else {
                for (let i = 0; i < lexeme.length; i++) {
                    const symbol = lexeme.charAt(i);
                    if (!(symbol >= 'A' && symbol <= 'Z')) {
                        return false;
                    }
                }
            }
        } else {
            if (!(lexeme.charAt(0) >= 'A' && lexeme.charAt(0) <= 'Z')) {
                return false;
            }
        }
        return true;
    }

    public app(): void {
        while (!this.isEnd()) {
            let word: string = '';
            this.startPosition.setLine(this.endPosition.getLine());
            this.startPosition.setColumn(this.endPosition.getColumn());
            while (true) {
                const currentChar = this.fragment.charAt(this.index);
                let lexeme: string;
                if (currentChar === '\'') {
                    if (!this.isNonTerminal(fragment.charAt(this.index - 1)))  {
                        while(this.fragment.charAt(this.index) !== '\n') {
                            const symbol = this.fragment.charAt(this.index);
                            word = word.concat(symbol);
                            this.index++;
                            this.endPosition.setNextColumn();
                        }
                        const newToken = new Token(TagName.COMMENT, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), word);
                        this.tokens.push(newToken);
                        this.index++;
                        this.endPosition.setNextLine();
                        this.endPosition.setColumn(1);
                        break;
                    }
                } else if (currentChar == '<') {
                    lexeme = word; 
                    if (lexeme.length !== 0) {
                        if (this.isKeyWord(lexeme)) {
                            const newToken = new Token(TagName.KEY_WORD, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        } else if (this.isTerminal(lexeme)) {
                            const newToken = new Token(TagName.TERMINAL, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        } else if (this.isNonTerminal(lexeme)) {
                            const newToken = new Token(TagName.NONTERMINAL, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        } else {
                            const newToken = new Token(TagName.ERROR, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        }
                    }
                    this.startPosition.setLine(this.endPosition.getLine());
                    this.startPosition.setColumn(this.endPosition.getColumn());
                    this.endPosition.setNextColumn();
                    const newToken = new Token(TagName.OPEN_TAG, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), '<');
                    this.tokens.push(newToken);
                    this.index++;
                    break;
                } else if (currentChar == '>') {
                    lexeme = word; 
                    if (lexeme.length !== 0) {
                        if (this.isKeyWord(lexeme)) {
                            const newToken = new Token(TagName.KEY_WORD, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        } else if (this.isTerminal(lexeme)) {
                            const newToken = new Token(TagName.TERMINAL, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        } else if (this.isNonTerminal(lexeme)) {
                            const newToken = new Token(TagName.NONTERMINAL, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        } else {
                            const newToken = new Token(TagName.ERROR, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        }
                    }
                    this.startPosition.setLine(this.endPosition.getLine());
                    this.startPosition.setColumn(this.endPosition.getColumn());
                    this.endPosition.setNextColumn();
                    const newToken = new Token(TagName.CLOSE_TAG, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), '>');
                    this.tokens.push(newToken);
                    this.index++;
                    break;
                } else if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
                    lexeme = word; 
                    if (lexeme.length != 0) {
                        if (this.isKeyWord(lexeme)) {
                            const newToken = new Token(TagName.KEY_WORD, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        } else if (this.isTerminal(lexeme)) {
                            const newToken = new Token(TagName.TERMINAL, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        } else if (this.isNonTerminal(lexeme)) {
                            const newToken = new Token(TagName.NONTERMINAL, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        } else {
                            const newToken = new Token(TagName.ERROR, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme); 
                            this.tokens.push(newToken);
                        }
                        if (currentChar == '\n') {
                            this.endPosition.setNextLine();
                            this.endPosition.setColumn(1);
                        } else {
                            this.endPosition.setNextColumn();
                        }
                    } else {
                        if (currentChar == '\n') {
                            this.endPosition.setNextLine();
                            this.endPosition.setColumn(1);
                        } else {
                            this.endPosition.setNextColumn();
                        }
                    }
                    this.index++;
                    break;
                } else if (!(currentChar >= 'a' && currentChar <= 'z') && !(currentChar >= 'A' && currentChar <= 'Z') &&
                    !(currentChar >= '0' && currentChar <= '9') && currentChar !== '\'' && currentChar !== '\(' 
                    && currentChar !== '\)' && currentChar !== '+' && currentChar !== '*') {
                    this.endPosition.setNextColumn();
                    this.index++;
                    const newToken = new Token(TagName.ERROR, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), word.concat(currentChar));
                    this.tokens.push(newToken);
                    this.messages.push(new Message(new Position(this.endPosition.getLine(), this.endPosition.getColumn()), "Unexpected characters"));
                    break;
                }
                word = word.concat(currentChar);
                this.index++;
                this.endPosition.setNextColumn();
                if (this.isEnd()) {
                    break;
                }
            }
        }
    }

    public getTokens(): Token[] {
        return this.tokens;
    }
    
}


const file = readFileSync("./test.txt", "utf-8");

const fragment: string = "' comment\n<axiom <E>>\n\n'grammar rules\n<E <T E'>>\n<E' <+ T E'>\n<>>\n<T <F T'>>\n<T' <* F T'>\n<>>\n<F <n>\n<( E )>>"
const lexer = new Lexer(file);
lexer.app();
const tokens = lexer.getTokens();
tokens.map(el => console.log(el.toString()))
