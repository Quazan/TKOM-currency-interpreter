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

public class ExpressionNodeTest {

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
    public void addIntToInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(5);
        initializeParser("2 + 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        IntNode actual = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void addDoubleToDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(5);
        initializeParser("1.5 + 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void addCurrencyToCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal("3.5"), "EUR", rates));
        Currency expectedValue = new Currency(new BigDecimal("5.0"), "EUR", rates);
        initializeParser("a + b");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        Currency actual = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void addDoubleToInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(4.5);
        initializeParser("1.5 + 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void addIntToDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(4.5);
        initializeParser("1 + 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void addIntToCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        initializeParser("a + 1");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void addDoubleToCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        initializeParser("a + 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void subtractIntFromInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(1);
        initializeParser("3 - 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        IntNode actual = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void subtractDoubleFromDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(2);
        initializeParser("3.5 - 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void subtractCurrencyFromCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("3.5"), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal("1.5"), "EUR", rates));
        Currency expectedValue = new Currency(new BigDecimal("2.0"), "EUR", rates);
        initializeParser("a - b");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        Currency actual = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void subtractDoubleFromInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(2.5);
        initializeParser("3.5 - 1");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void subtractIntFromDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5);
        initializeParser("3 - 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void subtractIntFromCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        initializeParser("a - 1");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void subtractDoubleFromCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        initializeParser("a - 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void multiplyIntByInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(6);
        initializeParser("2 * 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        IntNode actual = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void multiplyDoubleByDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 * 3.5);
        initializeParser("1.5 * 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void multiplyCurrencyByCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal("3.5"), "EUR", rates));
        initializeParser("a * b");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void multiplyDoubleByInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 * 3);
        initializeParser("1.5 * 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void multiplyIntByDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1 * 3.5);
        initializeParser("1 * 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void multiplyIntByCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        Currency expectedValue = new Currency(new BigDecimal("3.0"), "EUR", rates);
        initializeParser("a * 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        Currency actual = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void multiplyDoubleByCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("2"), "EUR", rates));
        Currency expectedValue = new Currency(new BigDecimal("5.0"), "EUR", rates);
        initializeParser("a * 2.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        Currency actual = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void divideIntByInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(0);
        initializeParser("2 / 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        IntNode actual = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void divideDoubleByDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 / 3.5);
        initializeParser("1.5 / 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void divideCurrencyByCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal("3.5"), "EUR", rates));
        initializeParser("a / b");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void divideDoubleByInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 / 3);
        initializeParser("1.5 / 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void divideIntByDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1 / 3.5);
        initializeParser("1 / 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        DoubleNode actual = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue(), 0);
    }

    @Test
    public void divideCurrencyByInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("1.5"), "EUR", rates));
        Currency expectedValue = new Currency(new BigDecimal("0.50000"), "EUR", rates);
        initializeParser("a / 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        Currency actual = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void divideCurrencyByDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("2.5"), "EUR", rates));
        Currency expectedValue = new Currency(new BigDecimal("1.00000"), "EUR", rates);
        initializeParser("a / 2.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        Currency actual = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
        assertEquals(expectedValue.getCurrencyType(), actual.getCurrencyType());
    }

    @Test
    public void correctArithmeticOrder() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(5);
        initializeParser("2 / 2 + 2 * 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        IntNode actual = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void correctArithmeticOrderWithParenthesis() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        IntNode expectedValue = new IntNode(4);
        initializeParser("(2 + 2) * 2 / 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        IntNode actual = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test
    public void expressionWithFunctionCall() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("int test() { return 2;}");
        parser.advance();
        Function function = parser.parseFunction();
        environment.addFunction(function);
        IntNode expectedValue = new IntNode(4);
        initializeParser("test() + test() * test() / test()");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        IntNode actual = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.getValue(), actual.getValue());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void divideIntByZero() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("2 / 0");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void divideDoubleByZero() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("2.5 / 0");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void divideDoubleByCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("2.5"), "EUR", rates));
        initializeParser("2.5 / a");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void divideIntByCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("2.5"), "EUR", rates));
        initializeParser("2 / a");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void divideCurrencyByZero() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal("2.5"), "EUR", rates));
        initializeParser("a / 0.");

        ExpressionNode expressionNode = (ExpressionNode) parser.parseExpression();
        expressionNode.evaluate(environment);
    }

}