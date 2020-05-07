package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.Expression;
import tkom.ast.Value;
import tkom.currency.Rates;
import tkom.execution.Environment;
import tkom.utils.NodeType;

import java.math.BigDecimal;

@Getter
public class Currency implements Expression, Value {

    private final BigDecimal value;

    private final String currencyType;

    private final Rates exchangeRates;

    public Currency(BigDecimal value, String currencyType, Rates exchangeRates) {
        this.currencyType = currencyType;
        this.exchangeRates = exchangeRates;
        this.value = exchangeRates.toEUR(currencyType, value);
    }

    @Override
    public NodeType getType() {
        return NodeType.CURRENCY;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return exchangeRates.toCurrency(currencyType, value) + " " + currencyType;
    }
}
