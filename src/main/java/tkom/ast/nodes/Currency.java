package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;

import java.math.BigDecimal;

@Data
public class Currency implements Node {

    private BigDecimal value;

    private String currencyType;

    @Override
    public NodeType getType() {
        return NodeType.CURRENCY;
    }
}
