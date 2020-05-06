package tkom.ast.nodes;

import org.junit.Before;
import org.junit.Test;
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

public class WhileStatementTest {
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
    public void returnStatementExitsLoop() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException, UndefinedReferenceException {
        ReturnCall expectedValue = new ReturnCall(new IntNode(3));
        environment.addVariable("a", new IntNode(0));
        initializeParser("while(a < 10){" +
                "if(a == 3) return a;" +
                "a = a + 1;}"
        );
        WhileStatement whileStatement = (WhileStatement) parser.parseStatement();

        ReturnCall actual = (ReturnCall) whileStatement.execute(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getReturnedValue().getType(), actual.getReturnedValue().getType());
        assertEquals(((IntNode) expectedValue.getReturnedValue()).getValue(),
                ((IntNode) actual.getReturnedValue()).getValue());
    }

    @Test(expected = UndefinedReferenceException.class)
    public void variablesInitializedInWhileLoopDoesNotExistAfter() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException, UndefinedReferenceException {
        environment.addVariable("a", new IntNode(0));
        initializeParser("while(a < 10){" +
                "int b = 3;" +
                "a = a + 3;}"
        );
        WhileStatement whileStatement = (WhileStatement) parser.parseStatement();

        whileStatement.execute(environment);
        environment.getVariable("b");

    }

    @Test
    public void whileLoopWithFalseCondition() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException, UndefinedReferenceException {
        IntNode expectedValue = new IntNode(0);
        environment.addVariable("a", expectedValue);
        initializeParser("while(2 < 0){" +
                "a = a + 3;}"
        );
        WhileStatement whileStatement = (WhileStatement) parser.parseStatement();

        whileStatement.execute(environment);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void whileLoopChangingOutsideVariable() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException, UndefinedReferenceException {
        IntNode expectedValue = new IntNode(10);
        environment.addVariable("a", new IntNode(0));
        initializeParser("while(a < 10){" +
                "a = a + 1;}"
        );
        WhileStatement whileStatement = (WhileStatement) parser.parseStatement();

        whileStatement.execute(environment);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }
}