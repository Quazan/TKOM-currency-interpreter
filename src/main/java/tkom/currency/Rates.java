package tkom.currency;

import java.util.List;
import java.util.Map;

public class Rates {

    private List<String> currencies;
    private Map<String, Double> exchange;

    public Rates(List<String> currencies, Map<String, Double> exchange) {
        this.currencies = currencies;
        this.exchange = exchange;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, Double> getExchange() {
        return exchange;
    }

    public void setExchange(Map<String, Double> exchange) {
        this.exchange = exchange;
    }

    public boolean contains(String currency) {
        return currencies.contains(currency);
    }
}
