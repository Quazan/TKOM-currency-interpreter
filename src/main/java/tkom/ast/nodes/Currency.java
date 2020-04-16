package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;

import java.math.BigDecimal;

@Getter
@Setter
public class Currency implements Node {

    private BigDecimal value;

    private String currencyType;

    @Override
    public NodeType getType() {
        return NodeType.CURRENCY;
    }
}
