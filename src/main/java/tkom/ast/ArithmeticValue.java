package tkom.ast;

import tkom.ast.nodes.Currency;
import tkom.ast.nodes.DoubleNode;
import tkom.ast.nodes.IntNode;
import tkom.error.RuntimeEnvironmentException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface ArithmeticValue extends Value{

    ArithmeticValue add(Value rightOperand)throws RuntimeEnvironmentException;

    ArithmeticValue subtract(Value rightOperand)throws RuntimeEnvironmentException;

    ArithmeticValue multiply(Value rightOperand) throws RuntimeEnvironmentException;

    ArithmeticValue divide(Value rightOperand)throws RuntimeEnvironmentException;

    static boolean isZero(Value value) throws RuntimeEnvironmentException {
        switch (value.getType()){
            case INT:
                return ((IntNode) value).getValue() == 0;
            case DOUBLE:
                return ((DoubleNode) value).getValue() == 0;
            case CURRENCY:
                return ((Currency) value).getValue().equals(BigDecimal.ZERO);
            default:
                throw new RuntimeEnvironmentException("Unexpected type");
        }
    }

    static private BigDecimal getBigDecimal(Value value) throws RuntimeEnvironmentException {
        switch (value.getType()){
            case INT:
                return new BigDecimal(((IntNode) value).getValue());
            case DOUBLE:
                return new BigDecimal(String.valueOf(((DoubleNode) value).getValue()));
            case CURRENCY:
                return ((Currency) value).getValue();
        }

        throw new RuntimeEnvironmentException("Cannot convert "  + value.getType() + " to Currency");
    }

    static BigDecimal multiplyBigDecimals(Value left, Value right) throws RuntimeEnvironmentException {
        return getBigDecimal(left).multiply(getBigDecimal(right));
    }

    static BigDecimal divideBigDecimals(Value left, Value right) throws RuntimeEnvironmentException {
        return getBigDecimal(left).divide(getBigDecimal(right), 5, RoundingMode.HALF_EVEN);
    }
}
