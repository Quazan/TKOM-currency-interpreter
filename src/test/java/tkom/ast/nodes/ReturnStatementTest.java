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

public class ReturnStatementTest {

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

    private void initializeParser(String tokenInput) {
        StringReader stringReader = new StringReader(tokenInput);
        Lexer lexer = new Lexer(stringReader, rates.getCurrencies());
        parser = new Parser(lexer);
    }

    @Before
    public void prepare() throws RuntimeEnvironmentException {
        prepareRates();
        prepareFunctions();
        environment = new Environment(functions, rates);
        environment.createNewScope();
    }

    @Test
    public void returnIntNode() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        ReturnCall expectedCall = new ReturnCall(new IntNode(10));
        initializeParser("return 10;");
        parser.advance();
        ReturnStatement returnStatement = parser.parseReturnStatement();

        ReturnCall actual = (ReturnCall) returnStatement.execute(environment);

        assertEquals(expectedCall.getType(), actual.getType());
        assertEquals(expectedCall.getReturnedValue().getType(), actual.getReturnedValue().getType());
        assertEquals(((IntNode) expectedCall.getReturnedValue()).getValue(),
                ((IntNode) actual.getReturnedValue()).getValue());
    }

    @Test
    public void returnDoubleNode() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        ReturnCall expectedCall = new ReturnCall(new DoubleNode(10.5));
        initializeParser("return 10.5;");
        parser.advance();
        ReturnStatement returnStatement = parser.parseReturnStatement();

        ReturnCall actual = (ReturnCall) returnStatement.execute(environment);

        assertEquals(expectedCall.getType(), actual.getType());
        assertEquals(expectedCall.getReturnedValue().getType(), actual.getReturnedValue().getType());
        assertEquals(((DoubleNode) expectedCall.getReturnedValue()).getValue(),
                ((DoubleNode) actual.getReturnedValue()).getValue(), 0);
    }

    @Test
    public void returnVariableWithCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        Currency currency = new Currency(new BigDecimal(1), "EUR", environment.getExchangeRates());
        ReturnCall expectedCall = new ReturnCall(currency);
        environment.addVariable("a", currency);
        initializeParser("return a;");
        parser.advance();
        ReturnStatement returnStatement = parser.parseReturnStatement();

        ReturnCall actual = (ReturnCall) returnStatement.execute(environment);

        assertEquals(expectedCall.getType(), actual.getType());
        assertEquals(expectedCall.getReturnedValue().getType(), actual.getReturnedValue().getType());
        assertEquals(((Currency) expectedCall.getReturnedValue()).getValue(),
                ((Currency) actual.getReturnedValue()).getValue());
    }

}