package tkom.ast.nodes;

import tkom.ast.Node;
import tkom.ast.NodeType;

public class Variable extends Signature implements Node {

    @Override
    public NodeType getType() {
        return NodeType.VARIABLE;
    }
}
