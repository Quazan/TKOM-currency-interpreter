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
        }
    };

    public static Map<String, TokenType> sings = new HashMap<>() {
        {
            put("(", TokenType.PARANT_OPEN);
            put(")", TokenType.PARANT_CLOSE);
            put("{", TokenType.CURLY_OPEN);
            put("}", TokenType.CURLY_CLOSE);
            put("[", TokenType.SQUARE_OPEN);
            put("]", TokenType.SQUARE_CLOSE;
            put("+", TokenType.PLUS);
            put("-", TokenType.MINUS);
            put("*", TokenType.MULTIPLY);
            put("/", TokenType.DIVIDE);
            put("=", TokenType.EQUALITY);
            put("!", TokenType.NEGATION);
            put("<", TokenType.LESS_THAN);
            put(">", TokenType.GREATER_THAN);
            put(",", TokenType.COMMA);
            put(";", TokenType.SEMICOLON);
        }
    };

}
