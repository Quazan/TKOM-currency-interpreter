package tkom.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
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

    public Token(int line, int position) {
        this.line = line;
        this.position = position;
    }

    public void setTypeAndValue(TokenType type, String value) {
        this.type = type;
        this.value = value;
        setNumericValue();
    }

}
