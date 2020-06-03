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
import tkom.utils.NodeType;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AssignStatementTest {

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
    public void assignNewIntValue() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        environment.addVariable("a", new IntNode(0));
        IntNode expectedValue = new IntNode(10);
        initializeParser("a = 10;");

        AssignStatement assignStatement = (AssignStatement) parser.parseStatement();
        assignStatement.execute(environment);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getType(), actual.getType());
    }

    @Test
    public void assignNewDoubleValue() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        environment.addVariable("a", new DoubleNode(0));
        DoubleNode expectedValue = new DoubleNode(10.5);
        initializeParser("a = 10.5;");

        AssignStatement assignStatement = (AssignStatement) parser.parseStatement();
        assignStatement.execute(environment);
        DoubleNode actual = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
        assertEquals(expectedValue.getType(), actual.getType());
    }

    @Test
    public void assignIntToDouble() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        environment.addVariable("a", new DoubleNode(0));
        IntNode expectedValue = new IntNode(10);
        initializeParser("a = 10;");

        AssignStatement assignStatement = (AssignStatement) parser.parseStatement();
        assignStatement.execute(environment);
        DoubleNode actual = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
        assertEquals(NodeType.DOUBLE, actual.getType());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void assignDoubleToInt() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        environment.addVariable("a", new IntNode(0));
        initializeParser("a = 10.5;");

        AssignStatement assignStatement = (AssignStatement) parser.parseStatement();
        assignStatement.execute(environment);
    }

    @Test
    public void assignNewCurrency() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        Currency currency = new Currency(new BigDecimal(1), "EUR", environment.getExchangeRates());
        environment.addVariable("a", currency);
        Currency expectedValue = new Currency(new BigDecimal(5), "EUR", environment.getExchangeRates());
        environment.addVariable("b", expectedValue);
        initializeParser("a = b;");

        AssignStatement assignStatement = (AssignStatement) parser.parseStatement();
        assignStatement.execute(environment);
        Currency actual = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getType(), actual.getType());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void assignIntToCurrency() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        Currency currency = new Currency(new BigDecimal(1), "EUR", environment.getExchangeRates());
        environment.addVariable("a", currency);
        initializeParser("a = 5;");

        AssignStatement assignStatement = (AssignStatement) parser.parseStatement();
        assignStatement.execute(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void assignDoubleToCurrency() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        Currency currency = new Currency(new BigDecimal(1), "EUR", environment.getExchangeRates());
        environment.addVariable("a", currency);
        initializeParser("a = 5.5;");

        AssignStatement assignStatement = (AssignStatement) parser.parseStatement();
        assignStatement.execute(environment);
    }

}