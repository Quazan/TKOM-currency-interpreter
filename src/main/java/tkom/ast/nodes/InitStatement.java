package tkom.ast.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
public class InitStatement extends Signature implements Statement, Node {

    private Expression assignable;

    @Override
    public NodeType getType() {
        return NodeType.INIT_STATEMENT;
    }
}
