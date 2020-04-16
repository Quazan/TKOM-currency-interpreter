package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Getter
@Setter
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
