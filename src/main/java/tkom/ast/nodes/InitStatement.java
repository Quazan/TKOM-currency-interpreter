package tkom.ast.nodes;

import lombok.*;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class InitStatement extends Signature implements Statement, Node {

    private Expression assignable;

    @Override
    public NodeType getType() {
        return NodeType.INIT_STATEMENT;
    }
}
