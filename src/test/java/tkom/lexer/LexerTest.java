package tkom.lexer;

import org.junit.BeforeClass;
import org.junit.Test;
import tkom.error.InvalidTokenException;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LexerTest {

    private static List<Token> expectedTokenList;
    private static List<String> currencies;
    private String emptyTestFilePath = "src/test/resources/emptyTest.txt";
    private String tokensTestFilePath = "src/test/resources/tokensTest.txt";
    private String keywordTestFilePath = "src/test/resources/keywordTokenTest.txt";
    private Lexer lexer;

    private void initializeLexer(Reader reader, List<String> currencies) {
        lexer = new Lexer(reader, currencies);
    }

    @BeforeClass
    public static void prepareExpectedTokenList() {
        int i = 0;
        expectedTokenList = new ArrayList<>();
        for (TokenType type: TokenType.values()) {
            Token token = new Token(type, "", i, 0);
            expectedTokenList.add(token);
            i++;
        }
    }

    @BeforeClass
    public static void prepareCurrencies() {
        currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("PLN");
    }


    private List<Token> getAllTokensFromFile() throws IOException, InvalidTokenException {
        List<Token> tokenList = new ArrayList<>();
        Token token;
        while((token=lexer.nextToken()).getType() != TokenType.END_OF_FILE){
            tokenList.add(token);
        }
        tokenList.add(token);
        return tokenList;
    }

    private void assertTokens(Token expected, Token actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getLine(), actual.getLine());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    @Test(expected = IOException.class)
    public void nextTokenOnClosedStream() throws IOException, InvalidTokenException {
        initializeLexer(new FileReader(emptyTestFilePath), null);

        lexer.nextToken();
        lexer.nextToken();
    }

    @Test
    public void nextTokenOnEmptyFile() throws IOException, InvalidTokenException {
        final Token expectedToken = new Token(TokenType.END_OF_FILE, "", 0, 0);
        initializeLexer(new FileReader(emptyTestFilePath), null);

        Token actual = lexer.nextToken();

        assertTokens(expectedToken, actual);
    }

    @Test
    public void nextTokenOnNullCurrencies() throws IOException, InvalidTokenException {
        final Token expectedToken = new Token(TokenType.RETURN, "", 0, 0);

        initializeLexer(new FileReader(keywordTestFilePath), null);

        Token actual = lexer.nextToken();

        assertTokens(expectedToken, actual);
    }

    @Test
    public void nextToken() throws IOException, InvalidTokenException {
        initializeLexer(new FileReader(tokensTestFilePath), currencies);

        List<Token> actual = getAllTokensFromFile();

        for(int i = 0; i < expectedTokenList.size(); i++) {
            assertTokens(expectedTokenList.get(i), actual.get(i));
        }
    }

}