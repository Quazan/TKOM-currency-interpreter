package tkom.lexer;

import org.junit.Test;
import tkom.error.InvalidTokenException;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LexerTest {

    private Lexer lexer;

    private void initializeLexer(String tokenInput) {
        StringReader stringReader = new StringReader(tokenInput);
        lexer = new Lexer(stringReader, null);
    }

    private void assertTokens(Token expected, Token actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getLine(), actual.getLine());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    @Test
    public void nextTokenOnNullCurrencies() throws IOException, InvalidTokenException {
        final Token expectedToken = new Token(TokenType.RETURN, "", 1, 1);

        initializeLexer("return");

        Token actual = lexer.nextToken();

        assertTokens(expectedToken, actual);
    }

    @Test(expected = IOException.class)
    public void nextTokenOnClosedStream() throws IOException, InvalidTokenException {
        StringReader stringReader = new StringReader("");
        lexer = new Lexer(stringReader, null);
        stringReader.close();

        lexer.nextToken();
    }

    @Test
    public void nextTokenOnEmptyFile() throws IOException, InvalidTokenException {
        final Token expectedToken = new Token(TokenType.END_OF_FILE, "", 1, 1);
        initializeLexer("");

        Token actual = lexer.nextToken();

        assertTokens(expectedToken, actual);
    }

    @Test(expected = InvalidTokenException.class)
    public void tooBigToken() throws IOException, InvalidTokenException {
        initializeLexer("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghija" +
                "bcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij");

        lexer.nextToken();
    }

    @Test(expected = InvalidTokenException.class)
    public void tooBigNumberToken() throws IOException, InvalidTokenException {
        initializeLexer("123456789012345678901234567890123567890");

        lexer.nextToken();
    }

    @Test(expected = InvalidTokenException.class)
    public void tooMuchPrecisionNumberToken() throws IOException, InvalidTokenException {
        initializeLexer("1.23456789012345678901234567890123567890123");

        lexer.nextToken();
    }

    @Test(expected = InvalidTokenException.class)
    public void invalidNumberToken() throws IOException, InvalidTokenException {
        initializeLexer("123..12.3");

        lexer.nextToken();
    }

    @Test(expected = InvalidTokenException.class)
    public void invalidToken() throws IOException, InvalidTokenException {
        initializeLexer("&=");

        lexer.nextToken();
    }

    @Test
    public void getTokenBeforeReadToken() {
        initializeLexer("");

        Token actual = lexer.getToken();

        assertNull(actual);
    }

    @Test
    public void validateNumberParsing() throws IOException, InvalidTokenException {
        final BigDecimal expectedNumberValue = new BigDecimal("123.567");
        initializeLexer(expectedNumberValue.toString());

        Token actual = lexer.nextToken();

        assertEquals(TokenType.NUMBER, actual.getType());
        assertEquals(expectedNumberValue, actual.getNumericValue());
    }
}