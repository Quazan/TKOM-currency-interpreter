package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class WhileStatement implements Statement, Node {

    private Condition condition;

    private StatementBlock whileBlock;

    @Override
    public NodeType getType() {
        return NodeType.WHILE_STATEMENT;
    }
}
