package com.compiler.SyntaxAnalysis;

import java.io.File;
import java.util.*;

public class RunApp {

    public static void main(String[] args) {

        try {
            File f = new File("E:\\Materials\\College Materials\\Fourth Year\\Second Term\\Compiler\\compiler\\compiler\\src\\test.txt");
            Scanner s = new Scanner(f);
            String value, token;
            int line, pos;
            Token t;
            boolean found;
            List<Token> list = new ArrayList<>();
            Map<String, TokenType> str_to_tokens = new HashMap<>();

            str_to_tokens.put("End_of_input", TokenType.End_of_input);
            str_to_tokens.put("Op_multiply", TokenType.Op_multiply);
            str_to_tokens.put("Op_divide", TokenType.Op_divide);
            str_to_tokens.put("Op_mod", TokenType.Op_mod);
            str_to_tokens.put("Op_add", TokenType.Op_add);
            str_to_tokens.put("Op_subtract", TokenType.Op_subtract);
            str_to_tokens.put("Op_negate", TokenType.Op_negate);
            str_to_tokens.put("Op_not", TokenType.Op_not);
            str_to_tokens.put("Op_less", TokenType.Op_less);
            str_to_tokens.put("Op_lessequal", TokenType.Op_lessequal);
            str_to_tokens.put("Op_greater", TokenType.Op_greater);
            str_to_tokens.put("Op_greaterequal", TokenType.Op_greaterequal);
            str_to_tokens.put("Op_equal", TokenType.Op_equal);
            str_to_tokens.put("Op_notequal", TokenType.Op_notequal);
            str_to_tokens.put("Op_assign", TokenType.Op_assign);
            str_to_tokens.put("Op_and", TokenType.Op_and);
            str_to_tokens.put("Op_or", TokenType.Op_or);
            str_to_tokens.put("Keyword_if", TokenType.Keyword_if);
            str_to_tokens.put("Keyword_else", TokenType.Keyword_else);
            str_to_tokens.put("Keyword_while", TokenType.Keyword_while);
            str_to_tokens.put("Keyword_print", TokenType.Keyword_print);
            str_to_tokens.put("Keyword_putc", TokenType.Keyword_putc);
            str_to_tokens.put("LeftParen", TokenType.LeftParen);
            str_to_tokens.put("RightParen", TokenType.RightParen);
            str_to_tokens.put("LeftBrace", TokenType.LeftBrace);
            str_to_tokens.put("RightBrace", TokenType.RightBrace);
            str_to_tokens.put("Semicolon", TokenType.Semicolon);
            str_to_tokens.put("Comma", TokenType.Comma);
            str_to_tokens.put("Identifier", TokenType.Identifier);
            str_to_tokens.put("Integer", TokenType.Integer);
            str_to_tokens.put("String", TokenType.String);


            String source = " ";
            while (s.hasNext()) {
                String str = s.nextLine();
                StringTokenizer st = new StringTokenizer(str);
                line = Integer.parseInt(st.nextToken());
                pos = Integer.parseInt(st.nextToken());
                token = st.nextToken();
                value = "";
                while (st.hasMoreTokens()) {
                    value += st.nextToken() + " ";
                }
                found = false;
                if (str_to_tokens.containsKey(token)) {
                    found = true;
                    list.add(new Token(str_to_tokens.get(token), value, line, pos));
                }
                if (found == false) {
                    throw new Exception("Token not found: '" + token + "'");
                }
            }
            Parser p = new Parser(list);
            p.printAST(p.parse());
        } catch(Exception e) {
            e.printStackTrace();
            Parser.error(-1, -1, "Exception: " + e.getMessage());
        }


    }
}
