package tkom.ast.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.ast.Statement;

@EqualsAndHashCode(callSuper = true)
@Data
public class InitStatement extends Signature implements Statement, Node {

    private Expression assignable;

    @Override
    public NodeType getType() {
        return NodeType.INIT_STATEMENT;
    }
}
