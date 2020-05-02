package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.execution.Environment;
import tkom.utils.NodeType;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Currency implements Expression, Value {

    private BigDecimal value;

    private String currencyType;

    @Override
    public NodeType getType() {
        return NodeType.CURRENCY;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }
}
