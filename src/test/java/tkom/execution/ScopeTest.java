package tkom.execution;

import org.junit.Before;
import org.junit.Test;
import tkom.ast.Value;
import tkom.ast.nodes.DoubleNode;
import tkom.ast.nodes.IntNode;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;

import static org.junit.Assert.*;

public class ScopeTest {

    private Scope scope;

    @Before
    public void prepare() {
        scope = new Scope();
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void afterAddingExistingVariableException() throws RuntimeEnvironmentException {
        scope.addVariable("a", new IntNode(0));

        scope.addVariable("a", new DoubleNode(0));
    }

    @Test
    public void getExistingVariable() throws RuntimeEnvironmentException {
        IntNode expectedVariable = new IntNode(0);
        scope.addVariable("a", expectedVariable);

        IntNode actual  = (IntNode) scope.getVariable("a");

        assertEquals(expectedVariable.getType(), actual.getType());
        assertEquals(expectedVariable.getValue(), actual.getValue());
    }

    @Test
    public void getNonExistingVariable() {
        Value actual  = scope.getVariable("a");

        assertNull(actual);
    }

    @Test
    public void containsVariableTrue() throws RuntimeEnvironmentException {
        scope.addVariable("a", new IntNode(0));

        assertTrue(scope.containsVariable("a"));
    }

    @Test
    public void containsVariableFalse() {
        assertFalse(scope.containsVariable("a"));
    }

    @Test
    public void setExistingVariable() throws RuntimeEnvironmentException, UndefinedReferenceException {
        IntNode expectedVariable = new IntNode(5);
        scope.addVariable("a", new IntNode(0));

        scope.setVariable("a", expectedVariable);
        IntNode actual = (IntNode) scope.getVariable("a");

        assertEquals(expectedVariable.getType(), actual.getType());
        assertEquals(expectedVariable.getValue(), actual.getValue());
    }

    @Test(expected = UndefinedReferenceException.class)
    public void setNonExistingVariable() throws UndefinedReferenceException {
        scope.setVariable("a", new IntNode(0));
    }

}