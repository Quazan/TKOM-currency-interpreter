package tkom.execution;

import org.junit.Before;
import org.junit.Test;
import tkom.ast.nodes.Function;
import tkom.ast.nodes.IntNode;
import tkom.currency.Rates;
import tkom.error.RuntimeEnvironmentException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EnvironmentTest {

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

    @Before
    public void prepare() throws RuntimeEnvironmentException {
        prepareRates();
        prepareFunctions();
        environment = new Environment(functions, rates);
        environment.createNewScope();
    }

    @Test
    public void containsCurrencyTrue() {
        assertTrue(environment.containsCurrency("EUR"));
    }

    @Test
    public void containsCurrencyFalse() {
        assertFalse(environment.containsCurrency("notactualcurrency"));
    }

    @Test
    public void getExistingFunction() {
        Function expectedFunction = new Function("int", "main");

        Function actual = environment.getFunction("main");

        assertEquals(expectedFunction.getIdentifier(), actual.getIdentifier());
        assertEquals(expectedFunction.getReturnType(), actual.getReturnType());
    }

    @Test
    public void getNonExistingFunction() {
        Function actual = environment.getFunction("notafunction");

        assertNull(actual);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void addMultipleFunctionsWithTheSameName() throws RuntimeEnvironmentException {
        environment = new Environment(new ArrayList<>() {{
            add(new Function("int", "test"));
            add(new Function("int", "test"));
        }}, rates);
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void addExistingVariable() throws RuntimeEnvironmentException {
        environment.addVariable("a", new IntNode(0));
        environment.addVariable("a", new IntNode(0));
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void addFunctionWithTheSameName() throws RuntimeEnvironmentException {
        environment.addFunction(new Function("int", "test"));
        environment.addFunction(new Function("int", "test"));
    }

    @Test
    public void getVariable() throws RuntimeEnvironmentException {
        IntNode expectedVariable = new IntNode(0);
        environment.addVariable("a", expectedVariable);

        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), actual.getValue());
        assertEquals(expectedVariable.getType(), actual.getType());
    }

    @Test
    public void getVariableFromParentScope() throws RuntimeEnvironmentException {
        IntNode expectedVariable = new IntNode(0);
        environment.addVariable("a", expectedVariable);
        environment.createNewLocalScope();

        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), actual.getValue());
        assertEquals(expectedVariable.getType(), actual.getType());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void getVariableFromNotAccessibleScope() throws RuntimeEnvironmentException {
        environment.addVariable("a", new IntNode(0));
        environment.createNewScope();

        environment.getVariable("a");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void getNonExistingVariable() throws RuntimeEnvironmentException {
        environment.getVariable("a");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void variableDontExistAfterScopeDestroy() throws RuntimeEnvironmentException {
        environment.createNewLocalScope();
        environment.addVariable("a", new IntNode(0));
        environment.destroyScope();

        environment.getVariable("a");
    }

    @Test
    public void setVariableToDifferentValue() throws RuntimeEnvironmentException {
        environment.addVariable("a", new IntNode(0));
        IntNode expectedVariable = new IntNode(5);

        environment.setVariable("a", expectedVariable);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), actual.getValue());
        assertEquals(expectedVariable.getType(), actual.getType());
    }

    @Test
    public void setVariableFromParentScopeToDifferentValue() throws RuntimeEnvironmentException {
        environment.addVariable("a", new IntNode(0));
        IntNode expectedVariable = new IntNode(5);
        environment.createNewLocalScope();

        environment.setVariable("a", expectedVariable);
        IntNode actual = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), actual.getValue());
        assertEquals(expectedVariable.getType(), actual.getType());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void setNonExistingVariable() throws RuntimeEnvironmentException {
        environment.setVariable("a", new IntNode(0));
    }


}