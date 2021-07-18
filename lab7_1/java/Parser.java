package lab7;

import lab7.AST.AstNode;
import lab7.AST.NonTermNode;
import lab7.AST.TermNode;
import lab7.Tokens.Token;
import lab7.Tokens.TokenEndFragment;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private ArrayList<Message> messages;
    private ArrayList<Token> tokens;
    private Stack<AstNode> stack;

    private final String[][][] TABLE = {
          //                  <                     >             term           kw                 n_term                  $
/* S */            {{"OPEN_TAG", "A", "S"},        null,          null,         null,                null,                {}   },
/* A */            { null,                         null,          null,   {"KEY_WORD", "B"},  {"NONTERMINAL", "F"},       null },
/* B */            {{"OPEN_TAG", "C", "D"},        null,          null,         null,                null,                null },
/* C */            { null,                         null,          null,         null,           {"NONTERMINAL"},          null },
/* D */            { null,                  {"CLOSE_TAG", "E"},   null,         null,                null,                null },
/* E */            { null,                    {"CLOSE_TAG"},      null,         null,                null,                null },
/* F */            {{"OPEN_TAG", "G", "H"},        null,          null,         null,                null,                null },
/* G */            { {},                            {},      {"TERMINAL", "G"}, null,         {"NONTERMINAL", "G"},       null },
/* H */            { null,                  {"CLOSE_TAG", "K"},   null,         null,                null,                null },
/* K */            {{"OPEN_TAG", "G", "H"},   {"CLOSE_TAG"},      null,         null,                null,                null }
    };

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.tokens.add(new TokenEndFragment(new Position(-1, -1), new Position(-1, -1)));
        this.tokens.removeIf(token1 -> token1.getTagName().equals("COMMENT"));
        this.messages = new ArrayList<>();
        this.stack = new Stack<>();
    }

    private int getIndexLine(String nonTerminal) {
        switch (nonTerminal) {
            case "S": return 0;
            case "A": return 1;
            case "B": return 2;
            case "C": return 3;
            case "D": return 4;
            case "E": return 5;
            case "F": return 6;
            case "G": return 7;
            case "H": return 8;
            case "K": return 9;
        }
        return -1;
    }

    private boolean check(String tagName) {
        return tagName.equals("OPEN_TAG") || tagName.equals("CLOSE_TAG") || tagName.equals("KEY_WORD") ||
                tagName.equals("NONTERMINAL") || tagName.equals("TERMINAL") || tagName.equals("END");
    }

    private int getIndexColumn(String tagName) {
        switch (tagName) {
            case "OPEN_TAG": return 0;
            case "CLOSE_TAG": return 1;
            case "TERMINAL": return 2;
            case "KEY_WORD": return 3;
            case "NONTERMINAL": return 4;
            case "END": return 5;
        }
        return -1;
    }

    private String getExpectedToken(String[][] lineInTable) {
        StringBuilder expectedTokens = new StringBuilder();
        for(int i = 0; i < lineInTable.length; i++) {
            if (lineInTable[i] != null && lineInTable[i].length != 0 && !lineInTable[i][0].equals("END") ) {
                expectedTokens.append(lineInTable[i][0]);
                expectedTokens.append(" or ");
            }
        }
        return expectedTokens.toString().length() == 0 ? "ERROR" : expectedTokens.delete(expectedTokens.lastIndexOf("or"), expectedTokens.length() - 1).toString();
    }


    public NonTermNode parse() {
        NonTermNode root = new NonTermNode("S");
        this.stack.push(root);
        while (!stack.empty()){
            AstNode node = stack.pop();
            Token token = this.tokens.get(0);
            if (node instanceof TermNode) {
                TermNode terminal = (TermNode) node;
                if(!terminal.getTagName().equals(token.getTagName())) {
                    this.messages.add(new Message(new Position(token.getEndPosition()), "Unexpected token: " + terminal.getToken() + " expected: " + token));
                    return root;
                }
                terminal.setToken(token);
                tokens.remove(0);
            } else {
                NonTermNode nonTerminal = (NonTermNode) node;
                String[] nodes = this.TABLE[this.getIndexLine(nonTerminal.getNonTermGrammar())][this.getIndexColumn(token.getTagName())];
                if (nodes == null) {
                    nonTerminal.addNode(new TermNode("ERROR"));
                    String expectedToken = this.getExpectedToken(this.TABLE[this.getIndexLine(nonTerminal.getNonTermGrammar())]);
                    this.messages.add(new Message(new Position(token.getEndPosition()), "Unexpected token: " + "[" + token
                            + "] expected Token: " + expectedToken));
                    return root;
                }
                for(int i = 0; i < nodes.length; i++) {
                    if (check(nodes[i])) {
                        nonTerminal.addNode(new TermNode(nodes[i]));
                    } else {
                        nonTerminal.addNode(new NonTermNode(nodes[i]));
                    }
                }
                for(int i = nonTerminal.getAstNode().size() - 1; i >= 0; i--) {
                    stack.push(nonTerminal.getAstNode().get(i));
                }
            }
        }
        return root;
    }


    public void printAST(AstNode astNode, int depth) {
        if (astNode instanceof NonTermNode) {
            for (AstNode child : ((NonTermNode) astNode).getAstNode()) {
                for (int indent = 2 * depth; indent > 0; indent--) {
                    System.out.print("..");
                }
                System.out.println("[" + astNode + "]" + " -> " + child);
                if (child instanceof NonTermNode) {
                    this.printAST(child, depth + 1);
                }
            }
        }
    }

    public void printMessages() {
        if (!messages.isEmpty()) {
            System.out.println("\nPARSING ERRORS:");
            for (Message message: this.messages) {
                System.out.println(message);
            }
        }
    }
}
