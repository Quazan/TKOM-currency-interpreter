package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.ArithmeticValue;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.currency.Rates;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

import java.math.BigDecimal;

@Getter
public class Currency implements Expression, ArithmeticValue {

    private final BigDecimal value;

    private final String currencyType;

    private final Rates exchangeRates;

    public Currency(BigDecimal value, String currencyType, Rates exchangeRates) {
        this.currencyType = currencyType;
        this.exchangeRates = exchangeRates;
        this.value = exchangeRates.toEUR(currencyType, value);
    }

    public Currency(Value value, String currencyType, Rates exchangeRates) throws RuntimeEnvironmentException {
        this.currencyType = currencyType;
        this.exchangeRates = exchangeRates;
        switch (value.getType()) {
            case INT:
                this.value = exchangeRates.toEUR(currencyType, new BigDecimal(Value.getIntValue(value)));
                break;
            case DOUBLE:
                this.value = exchangeRates.toEUR(currencyType,
                        new BigDecimal(String.valueOf(Value.getDoubleValue(value))));
                break;
            case CURRENCY:
                this.value = exchangeRates.toEUR(((Currency) value).getCurrencyType(), Value.getCurrencyValue(value));
                break;
            default:
                throw new RuntimeEnvironmentException("Cannot assign " + value.getType() + " to " + getType());
        }
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

    @Override
    public ArithmeticValue add(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isCurrency(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot add " + rightOperand.getType() + " to " +
                    getType());
        }

        return new Currency(value.add(Value.getCurrencyValue(rightOperand)),
                "EUR",
                exchangeRates);
    }

    @Override
    public ArithmeticValue subtract(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isCurrency(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot subtract " + rightOperand.getType() + " from " +
                    getType());
        }

        return new Currency(value.subtract(Value.getCurrencyValue(rightOperand)),
                "EUR",
                exchangeRates);
    }

    @Override
    public ArithmeticValue multiply(Value rightOperand) throws RuntimeEnvironmentException {
        if (!(Value.isInt(rightOperand) || Value.isDouble(rightOperand))) {
            throw new RuntimeEnvironmentException("Cannot multiply " + getType() + " by " +
                    rightOperand.getType());
        }

        return new Currency(ArithmeticValue.multiplyBigDecimals(this, rightOperand),
                "EUR", exchangeRates);
    }

    @Override
    public ArithmeticValue divide(Value rightOperand) throws RuntimeEnvironmentException {
        if (!(Value.isInt(rightOperand) || Value.isDouble(rightOperand))) {
            throw new RuntimeEnvironmentException("Cannot divide " + getType() + " by " +
                    rightOperand.getType());
        }

        if (ArithmeticValue.isZero(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot divide by 0");
        }

        return new Currency(ArithmeticValue.divideBigDecimals(this, rightOperand),
                "EUR", exchangeRates);
    }

    @Override
    public BoolNode isLess(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isCurrency(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                    " to " + getType());
        }

        return new BoolNode(value.compareTo(Value.getCurrencyValue(rightOperand)) < 0);
    }

    @Override
    public BoolNode isLessOrEqual(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isCurrency(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                    " to " + getType());
        }

        return new BoolNode(value.compareTo(Value.getCurrencyValue(rightOperand)) <= 0);
    }

    @Override
    public BoolNode isGrater(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isCurrency(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                    " to " + getType());
        }

        return new BoolNode(value.compareTo(Value.getCurrencyValue(rightOperand)) > 0);
    }

    @Override
    public BoolNode isGraterOrEqual(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isCurrency(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                    " to " + getType());
        }

        return new BoolNode(value.compareTo(Value.getCurrencyValue(rightOperand)) >= 0);
    }

    @Override
    public BoolNode isEqual(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isCurrency(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                    " to " + getType());
        }

        return new BoolNode(value.compareTo(Value.getCurrencyValue(rightOperand)) == 0);
    }
}
