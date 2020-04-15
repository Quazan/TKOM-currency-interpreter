package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.utils.Type;

@Data
public class Parameter implements Node {

    private Type type;

    private String name;

    @Override
    public NodeType getType() {
        return NodeType.PARAMETER;
    }
}
