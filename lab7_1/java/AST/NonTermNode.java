package lab7.AST;

import java.util.ArrayList;

public class NonTermNode extends AstNode {
    private String nonTermGrammar;
    private ArrayList<AstNode> astNode;

    public NonTermNode(String nonTermGrammar) {
        this.nonTermGrammar = nonTermGrammar;
        this.astNode = new ArrayList<>();
    }
    public void addNode(AstNode node) {
        this.astNode.add(node);
    }

    public String getNonTermGrammar() {
        return nonTermGrammar;
    }

    public ArrayList<AstNode> getAstNode() {
        return astNode;
    }

    @Override
    public String toString() {
        return "NonTerminalNode<" + this.nonTermGrammar + ">";
    }
}
