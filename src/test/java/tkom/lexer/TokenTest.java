package tkom.lexer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tkom.error.InvalidTokenException;
import tkom.utils.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TokenTest {

    private String token;
    private TokenType expectedTokenType;
    private Lexer lexer;
    //TODO add currencies
    private List<String> currencies;

    public TokenTest(String token, TokenType expectedTokenType, List<String> currencies) {
        this.expectedTokenType = expectedTokenType;
        this.token = token;
        this.currencies = currencies;
    }

    @Before
    public void prepare() {
        StringReader stringReader = new StringReader(token);
        lexer = new Lexer(stringReader, currencies);
    }

    //TODO add tokens and maybe values ??
    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] {
                {"int", TokenType.INT , },
        });
    }

    @Test
    public void testAllTokens() throws IOException, InvalidTokenException {
        assertEquals(expectedTokenType, lexer.nextToken().getType());
    }

}
