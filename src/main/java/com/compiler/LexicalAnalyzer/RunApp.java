package com.compiler.LexicalAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RunApp {

    public static void main(String[] args) {
        try {
            File f = new File("E:\\Materials\\College Materials\\Fourth Year\\Second Term\\Compiler\\compiler\\compiler\\src\\test.txt");
            Scanner s = new Scanner(f);
            String source = " ";
            while (s.hasNext()) {
                source += s.nextLine() + "\n";
            }
            Lexer l = new Lexer(source);
            l.printTokens();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            Lexer.error(-1, -1, "Exception: " + e.getMessage());
        }

    }
}
