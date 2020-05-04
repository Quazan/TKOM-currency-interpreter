package tkom.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class Rates {

    private List<String> currencies;
    private Map<String, BigDecimal> exchange;

    public Rates(List<String> currencies, Map<String, BigDecimal> exchange) {
        this.currencies = currencies;
        this.exchange = exchange;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, BigDecimal> getExchange() {
        return exchange;
    }

    public void setExchange(Map<String, BigDecimal> exchange) {
        this.exchange = exchange;
    }

    public boolean contains(String currency) {
        return currencies.contains(currency);
    }

    public BigDecimal toEUR(String type, BigDecimal value) {
        if(type.equals("EUR")) {
            return value;
        }
        return value.divide(exchange.get(type), 5, RoundingMode.HALF_EVEN);
    }

    public BigDecimal toBaseCurrency(String type, BigDecimal value) {
        if(type.equals("EUR")) {
            return value;
        }
        return value.multiply(exchange.get(type));
    }
}
