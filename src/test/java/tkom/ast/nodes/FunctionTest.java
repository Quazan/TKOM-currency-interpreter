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

public class FunctionTest {

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

    @Test(expected = RuntimeEnvironmentException.class)
    public void intFunctionWithoutReturnStatement() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(0);
        initializeParser("int test() {}");

        Function function = parser.parseFunction();
        IntNode actual = (IntNode) function.execute(environment, new ArrayList<>());

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void doubleFunctionWithReturnInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(0);
        initializeParser("double test() {return 0;}");

        Function function = parser.parseFunction();
        DoubleNode actual = (DoubleNode) function.execute(environment, new ArrayList<>());

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void functionWithArguments() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(5);
        initializeParser("double test(double d) {return d;}");

        Function function = parser.parseFunction();
        DoubleNode actual = (DoubleNode) function.execute(environment, new ArrayList<>() {{
            add(expectedValue);
        }});

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void functionCallWithWrongArgument() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("int test(int i) {return i;}");
        Function function = parser.parseFunction();
        environment.addFunction(function);
        initializeParser("test(10.5);");

        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        functionCall.execute(environment);
    }

    @Test
    public void functionCallWithCurrencyArguments() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        Currency expectedValue = new Currency(new BigDecimal(5), "EUR", rates);
        environment.addVariable("euro", expectedValue);
        initializeParser("EUR test(EUR e) {return e;}");
        Function function = parser.parseFunction();
        environment.addFunction(function);
        initializeParser("test(euro);");

        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        Currency actual = (Currency) functionCall.execute(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void functionCallWithDoubleArgumentsAndIntValue() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(5);
        initializeParser("double test(double d) {return d;}");
        Function function = parser.parseFunction();
        environment.addFunction(function);
        initializeParser("test(5);");

        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        DoubleNode actual = (DoubleNode) functionCall.execute(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void functionCallWithIncorrectNumberOfArguments() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("double test(double d, double c) {return d;}");
        Function function = parser.parseFunction();
        environment.addFunction(function);
        initializeParser("test(5);");

        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        functionCall.execute(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void functionCallNonExistingFunction() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("test(5);");

        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        functionCall.execute(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void functionWithMismatchingReturnType() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("int test(double d) {return d;}");
        Function function = parser.parseFunction();
        environment.addFunction(function);
        initializeParser("test(5);");

        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        functionCall.execute(environment);
    }


}