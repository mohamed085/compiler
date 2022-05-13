package com.compiler.LexicalAnalyzer;

enum TokenType {
    End_of_input, Op_multiply,  Op_divide, Op_mod, Op_add, Op_subtract,
    Op_negate, Op_not, Op_less, Op_lessequal, Op_greater, Op_greaterequal,
    Op_equal, Op_notequal, Op_assign, Op_and, Op_or, Keyword_if,
    Keyword_else, Keyword_while, Keyword_print, Keyword_put, LeftParen, RightParen,
    LeftBrace, RightBrace, Semicolon, Comma, Identifier, Integer, String
}
