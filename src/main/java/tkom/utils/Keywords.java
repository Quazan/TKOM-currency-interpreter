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
        }
    };

    public static Map<String, TokenType> singleSigns = new HashMap<>() {
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
            put("<", TokenType.LESS);
            put(">", TokenType.GREATER);
            put("!", TokenType.NOT);
            put(",", TokenType.COMMA);
            put(";", TokenType.SEMICOLON);
            put(".", TokenType.DOT);
        }
    };

    public static Map<String, TokenType> doubleSigns = new HashMap<>() {
        {
            put("!=", TokenType.INEQUALITY);
            put("==", TokenType.EQUALITY);
            put("<=", TokenType.LESS_OR_EQUAL);
            put(">=", TokenType.GREATER_OR_EQUAL);
            put("&&", TokenType.AND);
            put("||", TokenType.OR);
        }
    };
}
