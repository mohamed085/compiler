package com.compiler.SyntaxAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class Parser {
    private List<Token> source;
    private Token token;
    private int position;

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }
    Parser(List<Token> source) {
        this.source = source;
        this.token = null;
        this.position = 0;
    }
    Token getNextToken() {
        this.token = this.source.get(this.position++);
        return this.token;
    }
    Node expr(int p) {
        Node result = null, node;
        TokenType op;
        int q;

        if (this.token.tokentype == TokenType.LeftParen) {
            result = paren_expr();
        } else if (this.token.tokentype == TokenType.Op_add || this.token.tokentype == TokenType.Op_subtract) {
            op = (this.token.tokentype == TokenType.Op_subtract) ? TokenType.Op_negate : TokenType.Op_add;
            getNextToken();
            node = expr(TokenType.Op_negate.getPrecedence());
            result = (op == TokenType.Op_negate) ? Node.make_node(NodeType.nd_Negate, node) : node;
        } else if (this.token.tokentype == TokenType.Op_not) {
            getNextToken();
            result = Node.make_node(NodeType.nd_Not, expr(TokenType.Op_not.getPrecedence()));
        } else if (this.token.tokentype == TokenType.Identifier) {
            result = Node.make_leaf(NodeType.nd_Ident, this.token.value);
            getNextToken();
        } else if (this.token.tokentype == TokenType.Integer) {
            result = Node.make_leaf(NodeType.nd_Integer, this.token.value);
            getNextToken();
        } else {
            error(this.token.line, this.token.pos, "Expecting a primary, found: " + this.token.tokentype);
        }

        while (this.token.tokentype.isBinary() && this.token.tokentype.getPrecedence() >= p) {
            op = this.token.tokentype;
            getNextToken();
            q = op.getPrecedence();
            if (!op.isRightAssoc()) {
                q++;
            }
            node = expr(q);
            result = Node.make_node(op.getNodeType(), result, node);
        }
        return result;
    }
    Node paren_expr() {
        expect("paren_expr", TokenType.LeftParen);
        Node node = expr(0);
        expect("paren_expr", TokenType.RightParen);
        return node;
    }
    void expect(String msg, TokenType s) {
        if (this.token.tokentype == s) {
            getNextToken();
            return;
        }
        error(this.token.line, this.token.pos, msg + ": Expecting '" + s + "', found: '" + this.token.tokentype + "'");
    }
    Node stmt() {
        Node s, s2, t = null, e, v;
        if (this.token.tokentype == TokenType.Keyword_if) {
            getNextToken();
            e = paren_expr();
            s = stmt();
            s2 = null;
            if (this.token.tokentype == TokenType.Keyword_else) {
                getNextToken();
                s2 = stmt();
            }
            t = Node.make_node(NodeType.nd_If, e, Node.make_node(NodeType.nd_If, s, s2));
        } else if (this.token.tokentype == TokenType.Keyword_putc) {
            getNextToken();
            e = paren_expr();
            t = Node.make_node(NodeType.nd_Prtc, e);
            expect("Putc", TokenType.Semicolon);
        } else if (this.token.tokentype == TokenType.Keyword_print) {
            getNextToken();
            expect("Print", TokenType.LeftParen);
            while (true) {
                if (this.token.tokentype == TokenType.String) {
                    e = Node.make_node(NodeType.nd_Prts, Node.make_leaf(NodeType.nd_String, this.token.value));
                    getNextToken();
                } else {
                    e = Node.make_node(NodeType.nd_Prti, expr(0), null);
                }
                t = Node.make_node(NodeType.nd_Sequence, t, e);
                if (this.token.tokentype != TokenType.Comma) {
                    break;
                }
                getNextToken();
            }
            expect("Print", TokenType.RightParen);
            expect("Print", TokenType.Semicolon);
        } else if (this.token.tokentype == TokenType.Semicolon) {
            getNextToken();
        } else if (this.token.tokentype == TokenType.Identifier) {
            v = Node.make_leaf(NodeType.nd_Ident, this.token.value);
            getNextToken();
            expect("assign", TokenType.Op_assign);
            e = expr(0);
            t = Node.make_node(NodeType.nd_Assign, v, e);
            expect("assign", TokenType.Semicolon);
        } else if (this.token.tokentype == TokenType.Keyword_while) {
            getNextToken();
            e = paren_expr();
            s = stmt();
            t = Node.make_node(NodeType.nd_While, e, s);
        } else if (this.token.tokentype == TokenType.LeftBrace) {
            getNextToken();
            while (this.token.tokentype != TokenType.RightBrace && this.token.tokentype != TokenType.End_of_input) {
                t = Node.make_node(NodeType.nd_Sequence, t, stmt());
            }
            expect("LBrace", TokenType.RightBrace);
        } else if (this.token.tokentype == TokenType.End_of_input) {
        } else {
            error(this.token.line, this.token.pos, "Expecting start of statement, found: " + this.token.tokentype);
        }
        return t;
    }
    Node parse() {
        Node t = null;
        getNextToken();
        while (this.token.tokentype != TokenType.End_of_input) {
            t = Node.make_node(NodeType.nd_Sequence, t, stmt());
        }
        return t;
    }
    void printAST(Node t) {
        int i = 0;
        if (t == null) {
            System.out.println(";");
        } else {
            System.out.printf("%-14s", t.nt);
            if (t.nt == NodeType.nd_Ident || t.nt == NodeType.nd_Integer || t.nt == NodeType.nd_String) {
                System.out.println(" " + t.value);
            } else {
                System.out.println();
                printAST(t.left);
                printAST(t.right);
            }
        }
    }

}
