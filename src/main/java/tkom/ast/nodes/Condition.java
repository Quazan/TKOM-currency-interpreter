package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.utils.TokenType;

import java.util.List;

@Data
public class Condition implements Node {

    boolean negated = false;

    private TokenType operator;

    private List<Node> operands;

    public boolean isNegated() {
        return negated;
    }

    @Override
    public NodeType getType() {
        return NodeType.CONDITION;
    }
}
