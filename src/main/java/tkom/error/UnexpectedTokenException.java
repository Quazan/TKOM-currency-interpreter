package tkom.error;

import tkom.utils.TokenType;

import java.util.List;

public class UnexpectedTokenException extends Exception {
    public UnexpectedTokenException(int line, int position,
                                    TokenType expectedType,
                                    TokenType providedType) {
        super("Invalid token at line: " + line + " position: " + position + " expected Token: "
                + expectedType + " provided Token: " + providedType);
    }

    public UnexpectedTokenException(int line, int position,
                                    List<TokenType> expectedTypeList,
                                    TokenType providedType) {
        super("Invalid token at line: " + line + " position: " + position + " expected TokenTypes: "
                + expectedTypeList + " provided Token: " + providedType);
    }
}
