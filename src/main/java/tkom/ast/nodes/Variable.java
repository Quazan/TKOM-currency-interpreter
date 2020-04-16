package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Data
public class Variable implements Node {

    private String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public NodeType getType() {
        return NodeType.VARIABLE;
    }
}
