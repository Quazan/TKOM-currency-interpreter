package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;

@Data
public class Int implements Node {

    private int value;

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }
}
