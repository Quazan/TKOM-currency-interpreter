package tkom.parser;

import org.junit.Test;
import tkom.ast.Node;
import tkom.ast.nodes.*;
import tkom.error.InvalidTokenException;
import tkom.error.UnexpectedTokenException;
import tkom.lexer.Lexer;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    private void initializeParser(String tokenInput) {
        List<String> currencies = new ArrayList<>() {{
            add("EUR");
            add("PLN");
            add("USD");
        }};
        StringReader stringReader = new StringReader(tokenInput);
        Lexer lexer = new Lexer(stringReader, currencies);
        parser = new Parser(lexer);
    }


    @Test
    public void getExpectedToken() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "int a;";
        initializeParser(input);

        Token actual = parser.getToken(expectedToken.getType());

        assertEquals(expectedToken.getType(), actual.getType());
    }

    @Test(expected = UnexpectedTokenException.class)
    public void getUnexpectedToken() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "EUR euro;";
        initializeParser(input);

        parser.getToken(expectedToken.getType());

    }

    @Test()
    public void getOptionalTokenTrue() throws InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "int i;";
        initializeParser(input);

        boolean actual = parser.getOptionalToken(expectedToken.getType());

        assertTrue(actual);
    }

    @Test()
    public void getOptionalTokenFalse() throws InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "EUR euro;";
        initializeParser(input);

        boolean actual = parser.getOptionalToken(expectedToken.getType());

        assertFalse(actual);
    }

    @Test
    public void getOptionalTokenTypesTrue() throws IOException, InvalidTokenException {
        List<TokenType> expectedTokenTypes = new ArrayList<>() {{
          add(TokenType.INT);
        }};
        String input = "int i;";
        initializeParser(input);

        boolean actual = parser.getOptionalTokenTypes(expectedTokenTypes);

        assertTrue(actual);
    }

    @Test
    public void getOptionalTokenTypesFalse() throws IOException, InvalidTokenException {
        List<TokenType> expectedTokenTypes = new ArrayList<>() {{
            add(TokenType.INT);
        }};
        String input = "EUR euro;";
        initializeParser(input);

        boolean actual = parser.getOptionalTokenTypes(expectedTokenTypes);

        assertFalse(actual);
    }

    @Test
    public void parsePrimaryExpressionDouble() throws UnexpectedTokenException, InvalidTokenException, IOException {
        DoubleNode expectedNode = new DoubleNode();
        expectedNode.setValue(12.0);
        String input = "12.0";
        initializeParser(input);

        DoubleNode actual = (DoubleNode) parser.parsePrimaryExpression();

        assertEquals((Double) expectedNode.getValue(), (Double) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionNegativeDouble() throws UnexpectedTokenException, InvalidTokenException, IOException {
        DoubleNode expectedNode = new DoubleNode();
        expectedNode.setValue(-12.0);
        String input = "-12.0";
        initializeParser(input);

        DoubleNode actual = (DoubleNode) parser.parsePrimaryExpression();

        assertEquals((Double) expectedNode.getValue(), (Double) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionInt() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IntNode expectedNode = new IntNode();
        expectedNode.setValue(12);
        String input = "12";
        initializeParser(input);

        IntNode actual = (IntNode) parser.parsePrimaryExpression();

        assertEquals((Integer) expectedNode.getValue(), (Integer) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionNegativeInt() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IntNode expectedNode = new IntNode();
        expectedNode.setValue(-12);
        String input = "-12";
        initializeParser(input);

        IntNode actual = (IntNode) parser.parsePrimaryExpression();

        assertEquals((Integer) expectedNode.getValue(), (Integer) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionParenthesis() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Expression expectedNode = new Expression();
        String input = "(12 + 1)";
        initializeParser(input);

        Node actual = parser.parsePrimaryExpression();

        assertEquals(expectedNode.getType(), actual.getType());
    }

    @Test
    public void parsePrimaryExpressionIdentifierVariable() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Variable expectedNode = new Variable("a");
        String input = "a";
        initializeParser(input);

        Variable actual = (Variable) parser.parsePrimaryExpression();

        assertEquals(expectedNode.getType(), actual.getType());
        assertEquals(expectedNode.getIdentifier(), actual.getIdentifier());
    }

    @Test
    public void parsePrimaryExpressionIdentifierFunctionCall() throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall expectedNode = new FunctionCall();
        expectedNode.setIdentifier("min");
        String input = "min(a, 1 + 2, 2 * (a / 3))";
        initializeParser(input);

        FunctionCall actual = (FunctionCall) parser.parsePrimaryExpression();

        assertEquals(expectedNode.getType(), actual.getType());
        assertEquals(expectedNode.getIdentifier(), actual.getIdentifier());
    }

    @Test(expected = UnexpectedTokenException.class)
    public void parsePrimaryExpressionUnexpectedToken() throws UnexpectedTokenException, InvalidTokenException, IOException {
        String input = "return a;";
        initializeParser(input);

        parser.parsePrimaryExpression();
    }

    @Test
    public void parseMultiplicativeExpression() throws UnexpectedTokenException, InvalidTokenException, IOException {
        List<TokenType> expectedOperators = new ArrayList<>(){{
            add(TokenType.MULTIPLY);
            add(TokenType.DIVIDE);
        }};
        String input = "a * b / 2";
        initializeParser(input);

        Expression actual = parser.parseMultiplicativeExpression();

        assertEquals(expectedOperators.get(0), actual.getOperations().get(0));
        assertEquals(expectedOperators.get(1), actual.getOperations().get(1));
    }

    @Test
    public void parseAdditiveExpression() throws UnexpectedTokenException, InvalidTokenException, IOException {
        List<TokenType> expectedOperators = new ArrayList<>(){{
            add(TokenType.PLUS);
            add(TokenType.MINUS);
        }};
        String input = "a + b - 2";
        initializeParser(input);

        Expression actual = parser.parseExpression();

        assertEquals(expectedOperators.get(0), actual.getOperations().get(0));
        assertEquals(expectedOperators.get(1), actual.getOperations().get(1));
    }

}
