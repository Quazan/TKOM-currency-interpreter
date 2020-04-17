package tkom.lexer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tkom.error.InvalidTokenException;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TokenTest {

    private Lexer lexer;
    private final String tokenInput;
    private final TokenType expectedTokenType;
    private final String expectedValue;
    private final List<String> currencies;

    public TokenTest(String tokenInput, TokenType expectedTokenType, String expectedValue) {
        this.tokenInput = tokenInput;
        this.expectedTokenType = expectedTokenType;
        this.expectedValue = expectedValue;
        this.currencies = new ArrayList<>() {{
            add("EUR");
        }};
    }

    @Before
    public void prepare() {
        StringReader stringReader = new StringReader(tokenInput);
        lexer = new Lexer(stringReader, currencies);
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][]{
                {"if", TokenType.IF, "if"},
                {"else", TokenType.ELSE, "else"},
                {"while", TokenType.WHILE, "while"},
                {"return", TokenType.RETURN, "return"},
                {"int", TokenType.INT, "int"},
                {"double", TokenType.DOUBLE, "double"},
                {"EUR", TokenType.CURRENCY, "EUR"},
                {"123.5", TokenType.NUMBER, "123.5"},
                {"main", TokenType.IDENTIFIER, "main"},
                {"\"abcdef\"", TokenType.STRING, "\"abcdef\""},
                {"(", TokenType.ROUND_OPEN, ""},
                {")", TokenType.ROUND_CLOSE, ""},
                {"[", TokenType.SQUARE_OPEN, ""},
                {"]", TokenType.SQUARE_CLOSE, ""},
                {"{", TokenType.CURLY_OPEN, ""},
                {"}", TokenType.CURLY_CLOSE, ""},
                {"+", TokenType.PLUS, ""},
                {"-", TokenType.MINUS, ""},
                {"*", TokenType.MULTIPLY, ""},
                {"/", TokenType.DIVIDE, ""},
                {"=", TokenType.ASSIGNMENT, ""},
                {"<", TokenType.LESS, ""},
                {">", TokenType.GREATER, ""},
                {"!", TokenType.NOT, ""},
                {",", TokenType.COMMA, ""},
                {".", TokenType.DOT, ""},
                {";", TokenType.SEMICOLON, ""},
                {"!=", TokenType.INEQUALITY, ""},
                {"==", TokenType.EQUALITY, ""},
                {"<=", TokenType.LESS_OR_EQUAL, ""},
                {">=", TokenType.GREATER_OR_EQUAL, ""},
                {"&&", TokenType.AND, ""},
                {"||", TokenType.OR, ""},
                {"", TokenType.END_OF_FILE, ""},
        });
    }

    @Test
    public void testAllTokens() throws IOException, InvalidTokenException {
        Token currentToken = lexer.nextToken();

        assertEquals(expectedTokenType, currentToken.getType());
        assertEquals(expectedValue, currentToken.getValue());
    }

}
