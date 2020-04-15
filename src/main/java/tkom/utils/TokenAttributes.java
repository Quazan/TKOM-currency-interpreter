package tkom.utils;

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
    }};

}
