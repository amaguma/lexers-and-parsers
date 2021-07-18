package lab7.AST;

import lab7.Tokens.Token;

public class TermNode extends AstNode {
    private Token token;
    private String tagName;

    public TermNode(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TerminalNode: " + this.token;
    }
}
