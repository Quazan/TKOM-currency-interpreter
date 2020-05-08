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
import tkom.utils.ExecuteStatus;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IfStatementTest {
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
    public void trueCondition() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        ExecuteOut expectedValue = new ExecuteOut(ExecuteStatus.RETURN, new IntNode(42));
        initializeParser("if(2 < 4) return 42;" +
                "else return -1;");
        IfStatement ifStatement = (IfStatement) parser.parseStatement();

        ExecuteOut actual = (ExecuteOut) ifStatement.execute(environment);

        assertEquals(expectedValue.getStatus(), actual.getStatus());
        assertEquals(expectedValue.getReturnedValue().getType(), actual.getReturnedValue().getType());
        assertEquals(((IntNode) expectedValue.getReturnedValue()).getValue(),
                ((IntNode) actual.getReturnedValue()).getValue());
    }

    @Test
    public void falseCondition() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        ExecuteOut expectedValue = new ExecuteOut(ExecuteStatus.RETURN, new IntNode(42));
        initializeParser("if(2 > 4) return -1;" +
                "else return 42;");
        IfStatement ifStatement = (IfStatement) parser.parseStatement();

        ExecuteOut actual = (ExecuteOut) ifStatement.execute(environment);

        assertEquals(expectedValue.getStatus(), actual.getStatus());
        assertEquals(expectedValue.getReturnedValue().getType(), actual.getReturnedValue().getType());
        assertEquals(((IntNode) expectedValue.getReturnedValue()).getValue(),
                ((IntNode) actual.getReturnedValue()).getValue());
    }

    @Test
    public void ifStatementWithOnlyTrueBlock() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        ExecuteOut expectedValue = new ExecuteOut(ExecuteStatus.NORMAL);
        initializeParser("if(2 > 4) return -1;");
        IfStatement ifStatement = (IfStatement) parser.parseStatement();

        ExecuteOut actual = ifStatement.execute(environment);

        assertEquals(expectedValue.getStatus(), actual.getStatus());
        assertEquals(expectedValue.getReturnedValue(), actual.getReturnedValue());
    }

}