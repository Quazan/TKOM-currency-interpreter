package tkom.utils;

import java.math.BigDecimal;

public class Token {

    private TokenType type;
    private String value = "";
    private BigDecimal numericValue;
    private final int line;
    private final int position;

    private void setNumericValue() {
        if (type == TokenType.NUMBER) {
            numericValue = new BigDecimal(value);
        }
    }

    public Token(TokenType type, String value, Integer line, Integer position) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.position = position;
        setNumericValue();
    }

    public Token(TokenType type, int line, int position) {
        this.type = type;
        this.line = line;
        this.position = position;
    }

    public Token(int line, int position) {
        this.line = line;
        this.position = position;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Integer getLine() {
        return line;
    }

    public Integer getPosition() {
        return position;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", position=" + position +
                '}';
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTypeAndValue(TokenType type, String value) {
        this.type = type;
        this.value = value;
        setNumericValue();
    }
}
