package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class Int implements Node {

    private int value;

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }
}
