package tkom.ast.nodes;

import org.junit.Test;
import tkom.currency.Rates;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BoolNodeTest {

    @Test
    public void valueFromCurrencyTrue() {
        BoolNode boolNode = new BoolNode(new Currency(new BigDecimal(1),
                "EUR", new Rates(null, null)));
        assertTrue(boolNode.isValue());
    }

    @Test
    public void valueFromCurrencyFalse() {
        BoolNode boolNode = new BoolNode(new Currency(new BigDecimal(0),
                "EUR", new Rates(null, null)));
        assertFalse(boolNode.isValue());
    }

    @Test
    public void valueFromIntTrue() {
        BoolNode boolNode = new BoolNode(new IntNode(1));
        assertTrue(boolNode.isValue());
    }

    @Test
    public void valueFromIntFalse() {
        BoolNode boolNode = new BoolNode(new IntNode(0));
        assertFalse(boolNode.isValue());
    }

    @Test
    public void valueFromDoubleTrue() {
        BoolNode boolNode = new BoolNode(new DoubleNode(1.));
        assertTrue(boolNode.isValue());
    }

    @Test
    public void valueFromDoubleFalse() {
        BoolNode boolNode = new BoolNode(new DoubleNode(0.));
        assertFalse(boolNode.isValue());
    }


}