package tkom.ast.nodes;

import org.junit.Before;
import org.junit.Test;
import tkom.currency.Rates;
import tkom.error.InvalidTokenException;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UnexpectedTokenException;
import tkom.execution.Environment;
import tkom.lexer.Lexer;
import tkom.parser.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class InitStatementTest {

    private Parser parser;
    private Environment environment;
    private Rates rates;
    private List<Function> functions;

    private void prepareRates() {
        List<String> list = new ArrayList<>() {{
            add("EUR");
            add("PLN");
        }};

        Map<String, BigDecimal> exchange = new HashMap<>() {{
            put("PLN", new BigDecimal(4));
        }};

        this.rates = new Rates(list, exchange);
    }

    private void prepareFunctions() {
        this.functions = new ArrayList<>() {{
            add(new Function("int", "main"));
        }};
    }

    private void initializeParser(String tokenInput) throws IOException, InvalidTokenException {
        StringReader stringReader = new StringReader(tokenInput);
        Lexer lexer = new Lexer(stringReader, rates.getCurrencies());
        parser = new Parser(lexer);
        parser.advance();
    }

    @Before
    public void prepare() throws RuntimeEnvironmentException {
        prepareRates();
        prepareFunctions();
        environment = new Environment(functions, rates);
        environment.createNewScope();
    }

    @Test
    public void initNewIntVariable() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(10);
        initializeParser("int a = 10;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void initNewDoubleVariable() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(10.5);
        initializeParser("double a = 10.5;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        DoubleNode actual = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void initNewCurrencyVariableWithInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        Currency expectedValue = new Currency(new BigDecimal(5), "EUR", rates);
        initializeParser("EUR a = 5;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        Currency actual = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void initNewCurrencyVariableWithDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        Currency expectedValue = new Currency(new BigDecimal("5.5"), "EUR", rates);
        initializeParser("EUR a = 5.5;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        Currency actual = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void initNewIntVariableWithoutValue() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(0);
        initializeParser("int a;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void initNewDoubleVariableWithoutValue() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(0);
        initializeParser("double a;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        DoubleNode actual = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void initNewCurrencyVariableWithoutValue() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        Currency expectedValue = new Currency(new BigDecimal(0), "EUR", rates);
        initializeParser("EUR a;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        Currency actual = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void initNewDoubleVariableWithInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(10);
        initializeParser("double a = 10;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        DoubleNode actual = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void initNewIntVariableWithDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("int a = 10.5;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
    }

    @Test
    public void initNewIntVariableWithExistingVariable() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(10);
        environment.addVariable("b", expectedValue);
        initializeParser("int a = b;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void initNewCurrencyVariableWithExistingVariable() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        Currency expectedValue = new Currency(new BigDecimal(5), "EUR", rates);
        environment.addVariable("b", expectedValue);
        initializeParser("EUR a = b;");

        InitStatement initStatement = (InitStatement) parser.parseStatement();
        initStatement.execute(environment);
        Currency actual = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }


}