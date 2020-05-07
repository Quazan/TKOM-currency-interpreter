package tkom.currency;

import org.junit.Before;
import org.junit.Test;
import tkom.ast.nodes.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RatesTest {

    private Rates rates;

    @Before
    public void prepare() {
        List<String> list = new ArrayList<>() {{
            add("EUR");
            add("PLN");
        }};

        Map<String, BigDecimal> exchange = new HashMap<>() {{
            put("PLN", new BigDecimal(4));
        }};

        this.rates = new Rates(list, exchange);
    }

    @Test
    public void exchangeEURtoPLN() {
        BigDecimal expectedValue = new BigDecimal(4);

        BigDecimal actual = rates.toCurrency("PLN", new BigDecimal(1));

        assertEquals(expectedValue, actual);
    }

    @Test
    public void exchangeEURtoCurrencyEUR() {
        BigDecimal expectedValue = new BigDecimal(1);

        BigDecimal actual = rates.toCurrency("EUR", new BigDecimal(1));

        assertEquals(expectedValue, actual);
    }


    @Test
    public void exchangeEURtoEUR() {
        BigDecimal expectedValue = new BigDecimal(1);

        BigDecimal actual = rates.toEUR("EUR", new BigDecimal(1));

        assertEquals(expectedValue, actual);
    }

    @Test
    public void exchangePLNtoEUR() {
        BigDecimal expectedValue = new BigDecimal("1.00000");

        BigDecimal actual = rates.toEUR("PLN", new BigDecimal(4));

        assertEquals(expectedValue, actual);
    }

    @Test
    public void containsCurrencyTrue() {
        assertTrue(rates.contains("EUR"));
    }

    @Test
    public void containsCurrencyFalse() {
        assertFalse(rates.contains("notacurrency"));
    }


}