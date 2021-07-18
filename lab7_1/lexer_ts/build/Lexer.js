"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const Position_1 = require("./Position");
const Token_1 = require("./Token");
const Message_1 = require("./Message");
var TagName;
(function (TagName) {
    TagName["OPEN_TAG"] = "OPEN_TAG";
    TagName["CLOSE_TAG"] = "CLOSE_TAG";
    TagName["TERMINAL"] = "TERMINAL";
    TagName["KEY_WORD"] = "KEY_WORD";
    TagName["NONTERMINAL"] = "NONTERMINAL";
    TagName["COMMENT"] = "COMMENT";
    TagName["ERROR"] = "ERROR";
})(TagName || (TagName = {}));
class Lexer {
    constructor(fragment) {
        this.fragment = fragment;
        this.tokens = new Array();
        this.messages = new Array();
        this.startPosition = new Position_1.Position(1, 1);
        this.endPosition = new Position_1.Position(1, 1);
        this.index = 0;
    }
    isEnd() {
        return this.index > this.fragment.length - 1;
    }
    isKeyWord(lexeme) {
        return lexeme === 'axiom';
    }
    isTerminal(lexeme) {
        if (lexeme.length > 1) {
            for (let i = 0; i < lexeme.length; i++) {
                const symbol = lexeme.charAt(i);
                if (!(symbol >= 'a' && symbol <= 'z') && !(symbol >= '0' && symbol <= '9')) {
                    return false;
                }
            }
        }
        else {
            const symbol = lexeme.charAt(0);
            if (!(symbol >= 'a' && symbol <= 'z') && !(symbol >= '0' && symbol <= '9') && symbol !== '(' && symbol !== ')' && symbol !== '+' && symbol !== '*') {
                return false;
            }
        }
        return true;
    }
    isNonTerminal(lexeme) {
        if (lexeme.length > 1) {
            if (lexeme.charAt(lexeme.length - 1) === '\'') {
                for (let i = 0; i < lexeme.length - 1; i++) {
                    const symbol = lexeme.charAt(i);
                    if (!(symbol >= 'A' && symbol <= 'Z')) {
                        return false;
                    }
                }
            }
            else {
                for (let i = 0; i < lexeme.length; i++) {
                    const symbol = lexeme.charAt(i);
                    if (!(symbol >= 'A' && symbol <= 'Z')) {
                        return false;
                    }
                }
            }
        }
        else {
            if (!(lexeme.charAt(0) >= 'A' && lexeme.charAt(0) <= 'Z')) {
                return false;
            }
        }
        return true;
    }
    app() {
        while (!this.isEnd()) {
            let word = '';
            this.startPosition.setLine(this.endPosition.getLine());
            this.startPosition.setColumn(this.endPosition.getColumn());
            while (true) {
                const currentChar = this.fragment.charAt(this.index);
                let lexeme;
                if (currentChar === '\'') {
                    if (!this.isNonTerminal(fragment.charAt(this.index - 1))) {
                        while (this.fragment.charAt(this.index) !== '\n') {
                            const symbol = this.fragment.charAt(this.index);
                            word = word.concat(symbol);
                            this.index++;
                            this.endPosition.setNextColumn();
                        }
                        const newToken = new Token_1.Token(TagName.COMMENT, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), word);
                        this.tokens.push(newToken);
                        this.index++;
                        this.endPosition.setNextLine();
                        this.endPosition.setColumn(1);
                        break;
                        // if (!(this.isNonTerminal(fragment.charAt(this.index - 1)))) {
                        //     while(this.fragment.charAt(this.index) !== '\n') {
                        //         const symbol = this.fragment.charAt(this.index);
                        //         word = word.concat(symbol);
                        //         this.index++;
                        //         this.endPosition.setNextColumn();
                        //     }
                        //     const newToken = new Token(TagName.KEY_WORD, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), word);
                        //     this.tokens.push(newToken);
                        //     this.endPosition.setNextLine();
                        // }
                    }
                    // } else {
                    //     while(this.fragment.charAt(this.index) !== '\n') {
                    //         const symbol = this.fragment.charAt(this.index);
                    //         word = word.concat(symbol);
                    //         this.index++;
                    //         this.endPosition.setNextColumn();
                    //     }
                    //     const newToken = new Token(TagName.COMMENT, new Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position(this.endPosition.getLine(), this.endPosition.getColumn()), word);
                    //     this.tokens.push(newToken);
                    //     this.index++;
                    //     this.endPosition.setNextLine();
                    //     this.endPosition.setColumn(1);
                    //     break;
                    // }
                }
                else if (currentChar == '<') {
                    lexeme = word;
                    if (lexeme.length !== 0) {
                        if (this.isKeyWord(lexeme)) {
                            const newToken = new Token_1.Token(TagName.KEY_WORD, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else if (this.isTerminal(lexeme)) {
                            const newToken = new Token_1.Token(TagName.TERMINAL, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else if (this.isNonTerminal(lexeme)) {
                            const newToken = new Token_1.Token(TagName.NONTERMINAL, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else {
                            const newToken = new Token_1.Token(TagName.ERROR, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                    }
                    this.startPosition.setLine(this.endPosition.getLine());
                    this.startPosition.setColumn(this.endPosition.getColumn());
                    this.endPosition.setNextColumn();
                    const newToken = new Token_1.Token(TagName.OPEN_TAG, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), '<');
                    this.tokens.push(newToken);
                    this.index++;
                    break;
                }
                else if (currentChar == '>') {
                    lexeme = word;
                    if (lexeme.length !== 0) {
                        if (this.isKeyWord(lexeme)) {
                            const newToken = new Token_1.Token(TagName.KEY_WORD, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else if (this.isTerminal(lexeme)) {
                            const newToken = new Token_1.Token(TagName.TERMINAL, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else if (this.isNonTerminal(lexeme)) {
                            const newToken = new Token_1.Token(TagName.NONTERMINAL, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else {
                            const newToken = new Token_1.Token(TagName.ERROR, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                    }
                    this.startPosition.setLine(this.endPosition.getLine());
                    this.startPosition.setColumn(this.endPosition.getColumn());
                    this.endPosition.setNextColumn();
                    const newToken = new Token_1.Token(TagName.CLOSE_TAG, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), '>');
                    this.tokens.push(newToken);
                    this.index++;
                    break;
                }
                else if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
                    lexeme = word;
                    if (lexeme.length != 0) {
                        if (this.isKeyWord(lexeme)) {
                            const newToken = new Token_1.Token(TagName.KEY_WORD, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else if (this.isTerminal(lexeme)) {
                            const newToken = new Token_1.Token(TagName.TERMINAL, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else if (this.isNonTerminal(lexeme)) {
                            const newToken = new Token_1.Token(TagName.NONTERMINAL, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        else {
                            const newToken = new Token_1.Token(TagName.ERROR, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), lexeme);
                            this.tokens.push(newToken);
                        }
                        if (currentChar == '\n') {
                            this.endPosition.setNextLine();
                            this.endPosition.setColumn(1);
                        }
                        else {
                            this.endPosition.setNextColumn();
                        }
                    }
                    else {
                        if (currentChar == '\n') {
                            this.endPosition.setNextLine();
                            this.endPosition.setColumn(1);
                        }
                        else {
                            this.endPosition.setNextColumn();
                        }
                    }
                    this.index++;
                    break;
                }
                else if (!(currentChar >= 'a' && currentChar <= 'z') && !(currentChar >= 'A' && currentChar <= 'Z') &&
                    !(currentChar >= '0' && currentChar <= '9') && currentChar !== '\'' && currentChar !== '\('
                    && currentChar !== '\)' && currentChar !== '+' && currentChar !== '*') {
                    this.endPosition.setNextColumn();
                    this.index++;
                    const newToken = new Token_1.Token(TagName.ERROR, new Position_1.Position(this.startPosition.getLine(), this.startPosition.getColumn()), new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), word.concat(currentChar));
                    this.tokens.push(newToken);
                    this.messages.push(new Message_1.Message(new Position_1.Position(this.endPosition.getLine(), this.endPosition.getColumn()), "Unexpected characters"));
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
    // public app(): void{
    //     while(this.lineNumber < this.fragment.split("\n").length) {
    //         this.line = this.nextLine();
    //         this.lineNumber++;
    //         this.endPosition.setLine(this.lineNumber);
    //         this.startPosition.setLine(this.endPosition.getLine());
    //         this.startPosition.setColumn(this.endPosition.getColumn())
    //         while (this.line.length != 0) {
    //             let tagName = "Syntax error";
    //             let m = this.p.exec(this.line);
    //             if (m && m.groups) {
    //                 let tokenName: string;
    //                 if (m.groups.comment != null) {
    //                     tagName = TagName.COMMENT;
    //                     tokenName = "comment";
    //                 } else if (m.groups.openTag != null) {
    //                     tagName = TagName.OPEN_TAG;
    //                     tokenName = "openTag";
    //                 } else if (m.groups.closeTag != null) {
    //                     tagName = TagName.CLOSE_TAG;
    //                     tokenName = "closeTag";
    //                 } else if (m.groups.keyword != null) {
    //                     tagName = TagName.KEY_WORD;
    //                     tokenName = "keyword";
    //                 } else if (m.groups.terminal != null) {
    //                     tagName = TagName.TERMINAL;
    //                     tokenName = "terminal";
    //                 } else {
    //                     tagName = TagName.NONTERMINAL;
    //                     tokenName = "nonterminal";
    //                 }
    //                 this.endPosition.setColumn(m.indexOf(tokenName) + m.groups.opentag.length); 
    //                 this.tokens.push(new Token(tagName, new Position(this.startPosition.getLine(), this.startPosition.getColumn()),
    //                 new Position(this.endPosition.getLine(), this.endPosition.getColumn()), m.groups.tokenName));
    //                 // this.columnNumber += m.start(tokenName) + m.group(tokenName).length();
    //                 this.line = this.line.substring(m.indexOf(tokenName) + m.groups.tokenName.length);
    //             } else {
    //                 this.tokens.push(new Token(tagName, new Position(this.startPosition.getLine(), this.startPosition.getColumn()),new Position(this.endPosition.getLine(), this.endPosition.getColumn()), "" ));
    //                 while (this.line.length != 0 && !this.p.test(this.line)) {
    //                     this.line = this.line.substring(1);
    //                     this.endPosition.setNextColumn();
    //                 }
    //             }
    //         }
    //     }
    // }
    // private hasNextToken(): boolean {
    //     return !this.tokens.;
    // }
    // private nextToken(): Token {
    //     // return this.tokens.remove(0);
    //     return this.tokens.p
    // }
    // private static String readFile(String fileName) {
    //     StringBuilder builder = new StringBuilder();
    //     String line;
    //     try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
    //         while ((line = in.readLine()) != null) {
    //             builder.append(line).append("\n");
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return builder.toString();
    // }
    getTokens() {
        return this.tokens;
    }
}
// const file = readFileSync("./test.txt", "utf-8");
const fragment = "' comment\n<axiom <E>>\n\n'grammar rules\n<E <T E'>>\n<E' <+ T E'>\n<>>\n<T <F T'>>\n<T' <* F T'>\n<>>\n<F <n>\n<( E )>>";
const lexer = new Lexer(fragment);
lexer.app();
const tokens = lexer.getTokens();
tokens.map(el => console.log(el.toString()));
