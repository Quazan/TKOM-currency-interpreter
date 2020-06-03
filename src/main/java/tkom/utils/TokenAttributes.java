package tkom.utils;

import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

public final class TokenAttributes {
    public static List<TokenType> valueTypes = new ArrayList<>() {{
        add(TokenType.INT);
        add(TokenType.DOUBLE);
        add(TokenType.CURRENCY);
    }};

    public static List<TokenType> statementTypes = new ArrayList<>() {{
        add(TokenType.INT);
        add(TokenType.DOUBLE);
        add(TokenType.CURRENCY);
        add(TokenType.IF);
        add(TokenType.WHILE);
        add(TokenType.RETURN);
        add(TokenType.IDENTIFIER);
        add(TokenType.PRINT);
    }};

    public static List<TokenType> primaryExpressionTypes = new ArrayList<>() {{
        add(TokenType.MINUS);
        add(TokenType.NUMBER);
        add(TokenType.ROUND_OPEN);
        add(TokenType.IDENTIFIER);
    }};

    public static List<TokenType> multiplicativeOperators = new ArrayList<>() {{
        add(TokenType.MULTIPLY);
        add(TokenType.DIVIDE);
    }};

    public static List<TokenType> additiveOperators = new ArrayList<>() {{
        add(TokenType.PLUS);
        add(TokenType.MINUS);
    }};

    public static List<TokenType> equalityOperators = new ArrayList<>() {{
        add(TokenType.EQUALITY);
        add(TokenType.INEQUALITY);
    }};

    public static List<TokenType> relationOperators = new ArrayList<>() {{
        add(TokenType.LESS);
        add(TokenType.LESS_OR_EQUAL);
        add(TokenType.GREATER);
        add(TokenType.GREATER_OR_EQUAL);
    }};
}
