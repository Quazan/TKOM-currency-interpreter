package tkom.ast.nodes;

import org.junit.Before;
import org.junit.Test;
import tkom.ast.Statement;
import tkom.currency.Rates;
import tkom.error.InvalidTokenException;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
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

public class StatementBlockTest {
    private Parser parser;
    private Environment environment;
    private Rates rates;
    private List<Function> functions;

    private void prepareRates(){
        List<String> list = new ArrayList<>() {{
            add("EUR");
            add("PLN");
        }};

        Map<String, BigDecimal> exchange = new HashMap<>(){{
            put("PLN", new BigDecimal(4));
        }};

        this.rates = new Rates(list, exchange);
    }

    private void prepareFunctions(){
        this.functions = new ArrayList<>(){{
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
    public void afterReturnStatementExecutionIsCancelled() throws IOException, InvalidTokenException, UnexpectedTokenException, UndefinedReferenceException, RuntimeEnvironmentException {
        ReturnCall expectedValue = new ReturnCall(new IntNode(2));
        initializeParser("{int a = 0;" +
                "a = a + 2;" +
                "return a;" +
                "a = a + 2;" +
                "return a;}");
        StatementBlock statementBlock = parser.parseStatementBlock();

        ReturnCall actual = (ReturnCall) statementBlock.execute(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(((IntNode) expectedValue.getReturnedValue()).getValue(),
                ((IntNode) actual.getReturnedValue()).getValue());
        assertEquals(expectedValue.getReturnedValue().getType(),
                actual.getReturnedValue().getType());
    }

    @Test
    public void emptyStatementBlock() throws IOException, InvalidTokenException, UnexpectedTokenException, UndefinedReferenceException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(0);
        initializeParser("{}");
        StatementBlock statementBlock = parser.parseStatementBlock();

        IntNode actual = (IntNode) statementBlock.execute(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void returnCallsArePropagated() throws IOException, InvalidTokenException, UnexpectedTokenException, UndefinedReferenceException, RuntimeEnvironmentException {
        ReturnCall expectedValue = new ReturnCall(new IntNode(0));
        initializeParser("{int a = 0;" +
                "if(a == 0) return a;}");
        StatementBlock statementBlock = parser.parseStatementBlock();

        ReturnCall actual = (ReturnCall) statementBlock.execute(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(((IntNode) expectedValue.getReturnedValue()).getValue(),
                ((IntNode) actual.getReturnedValue()).getValue());
        assertEquals(expectedValue.getReturnedValue().getType(),
                actual.getReturnedValue().getType());
    }
}