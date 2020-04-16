package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Getter
@Setter
public class Int implements Node {

    private int value;

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }
}
