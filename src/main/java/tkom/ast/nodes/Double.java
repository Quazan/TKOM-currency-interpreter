package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Data
public class Double implements Node {

    private double value;

    @Override
    public NodeType getType() {
        return null;
    }
}
