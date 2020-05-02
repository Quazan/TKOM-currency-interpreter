package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.utils.NodeType;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Currency implements Node, Expression {

    private BigDecimal value;

    private String currencyType;

    @Override
    public NodeType getType() {
        return NodeType.CURRENCY;
    }
}
