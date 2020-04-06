package tkom.utils;

import java.util.HashMap;
import java.util.Map;

public final class Keywords {

    public static Map<String, TokenType> keywords = new HashMap<>() {
        {
            put("if", TokenType.IF);
            put("else", TokenType.ELSE);
            put("while", TokenType.WHILE);
            put("return", TokenType.RETURN);
            put("int", TokenType.INT);
            put("double", TokenType.DOUBLE);
            //add currency from file
        }
    };

    public static Map<String, TokenType> singleSings = new HashMap<>() {
        {
            put("(", TokenType.ROUND_OPEN);
            put(")", TokenType.ROUND_CLOSE);
            put("{", TokenType.CURLY_OPEN);
            put("}", TokenType.CURLY_CLOSE);
            put("[", TokenType.SQUARE_OPEN);
            put("]", TokenType.SQUARE_CLOSE);
            put("+", TokenType.PLUS);
            put("-", TokenType.MINUS);
            put("*", TokenType.MULTIPLY);
            put("/", TokenType.DIVIDE);
            put("=", TokenType.ASSIGNMENT);
            put("!", TokenType.NOT);
            put("<", TokenType.LESS);
            put(">", TokenType.GREATER);
            put(",", TokenType.COMMA);
            put(";", TokenType.SEMICOLON);
            put(".", TokenType.DOT);
        }
    };

}
