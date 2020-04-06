package tkom;

import tkom.lexer.Lexer;
import tkom.utils.Token;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("src/main/resources/test.txt");
        Token token;
        while ((token=lexer.nextToken()) != null) {
            System.out.println(token);
        }
    }
}
