package tkom.currency;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rates {

    @Getter
    private final List<String> currencies;

    private final Map<String, BigDecimal> exchange;

    public Rates(List<String> currencies, Map<String, BigDecimal> exchange) {
        this.currencies = currencies;
        this.exchange = exchange;
    }

    public Rates() {
        this.currencies = new ArrayList<>();
        this.exchange = new HashMap<>();
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

    public BigDecimal toCurrency(String type, BigDecimal value) {
        if(type.equals("EUR")) {
            return value;
        }
        return value.multiply(exchange.get(type));
    }
}
