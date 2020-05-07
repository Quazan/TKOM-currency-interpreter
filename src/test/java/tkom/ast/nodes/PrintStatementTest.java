package tkom.ast.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tkom.currency.Rates;
import tkom.error.RuntimeEnvironmentException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PrintStatementTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    public void printMultipleValues() throws RuntimeEnvironmentException {
        PrintStatement print = new PrintStatement();
        print.addArgument(new IntNode(0));
        print.addArgument(new IntNode(0));
        print.addArgument(new IntNode(0));
        print.addArgument(new IntNode(0));
        print.addArgument(new StringNode("test"));

        print.execute(null);

        assertEquals("0000test\r\n", outContent.toString());
    }

    @Test
    public void printInt() throws RuntimeEnvironmentException {
        PrintStatement print = new PrintStatement();
        print.addArgument(new IntNode(1));

        print.execute(null);

        assertEquals("1\r\n", outContent.toString());
    }

    @Test
    public void printDouble() throws RuntimeEnvironmentException {
        PrintStatement print = new PrintStatement();
        print.addArgument(new DoubleNode(2.));

        print.execute(null);

        assertEquals("2.0\r\n", outContent.toString());
    }

    @Test
    public void printCurrency() throws RuntimeEnvironmentException {
        PrintStatement print = new PrintStatement();
        print.addArgument(new Currency(new BigDecimal(3), "EUR", new Rates()));

        print.execute(null);

        assertEquals("3 EUR\r\n", outContent.toString());
    }

    @Test
    public void printString() throws RuntimeEnvironmentException {
        PrintStatement print = new PrintStatement();
        print.addArgument(new StringNode("test"));

        print.execute(null);

        assertEquals("test\r\n", outContent.toString());
    }

}