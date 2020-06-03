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

import static org.junit.Assert.assertEquals;

public class ConditionTest {

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
    public void isEqualIntToInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5 == 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isEqualDoubleToDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5.5 == 5.5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isEqualCurrencyToCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(5), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal(5), "EUR", rates));
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("a == b");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isEqualIntToDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5 == 5.");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isEqualDoubleToInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("5.5 == 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isEqualCurrencyToInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(5), "EUR", rates));
        initializeParser("a == 5");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test
    public void isEqualBoolToBool() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser(" 1 < 2 == 2 < 3");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isNotEqual() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5 != 4");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessIntThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("4 < 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessDoubleThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("4.5 < 5.5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessCurrencyThanCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(4), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal(5), "EUR", rates));
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("a < b");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessIntThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("4 < 5.");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessDoubleThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("2.5 < 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isLessCurrencyThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(5), "EUR", rates));
        initializeParser("a < 5");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isLessBoolThanBool() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser(" (1 < 2) < (2 < 3)");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test
    public void isLessOrEqualIntThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5 <= 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessOrEqualDoubleThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("4.5 <= 5.5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessOrEqualCurrencyThanCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(4), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal(5), "EUR", rates));
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("a <= b");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessOrEqualIntThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("4 <= 4.");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isLessOrEqualDoubleThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5. <= 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isLessOrEqualCurrencyThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(5), "EUR", rates));
        initializeParser("a <= 5");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isLessOrEqualBoolThanBool() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser(" (1 < 2) <= (2 < 3)");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test
    public void isGraterOrEqualIntThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4 >= 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterOrEqualDoubleThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4.5 >= 5.5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterOrEqualCurrencyThanCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(4), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal(5), "EUR", rates));
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("a >= b");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterOrEqualIntThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4 >= 5.");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterOrEqualDoubleThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4. >= 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isGraterOrEqualCurrencyThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(5), "EUR", rates));
        initializeParser("a >= 5");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isGraterOrEqualBoolThanBool() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser(" (1 < 2) >= (2 < 3)");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test
    public void isGraterIntThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4 > 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterDoubleThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4.5 > 5.5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterCurrencyThanCurrency() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(4), "EUR", rates));
        environment.addVariable("b", new Currency(new BigDecimal(5), "EUR", rates));
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("a > b");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterIntThanDouble() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4 > 5.");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void isGraterDoubleThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("4. > 5");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isGraterCurrencyThanInt() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(5), "EUR", rates));
        initializeParser("a > 5");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void isGraterBoolThanBool() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser(" (1 < 2) > (2 < 3)");
        Condition condition = parser.parseCondition();

        condition.evaluate(environment);
    }

    @Test
    public void andMultiple() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(2), "EUR", rates));
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5 && 5.5 && a");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void orMultiple() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(2), "EUR", rates));
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5 || 5.5 || a");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void negation() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(2), "EUR", rates));
        BoolNode expectedValue = new BoolNode(false);
        initializeParser("!(5 && 5.5 && a)");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void expressionInCondition() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(2), "EUR", rates));
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("a != a * 2");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void functionCallInCondition() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        initializeParser("int test(){ return 2;}");
        parser.advance();
        Function function = parser.parseFunction();
        environment.addFunction(function);
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("test() != test() * 2");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }

    @Test
    public void checkCorrectOrder() throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        environment.addVariable("a", new Currency(new BigDecimal(2), "EUR", rates));
        BoolNode expectedValue = new BoolNode(true);
        initializeParser("5 || 0 < 3 && 3 <= 4 || a != a * 2");
        Condition condition = parser.parseCondition();

        BoolNode actual = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), actual.getType());
        assertEquals(expectedValue.isValue(), actual.isValue());
    }


}