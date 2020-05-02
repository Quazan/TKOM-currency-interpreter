package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class ReturnStatement implements Statement, Node {

    private ExpressionNode expressionNode;

    public ReturnStatement(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    @Override
    public NodeType getType() {
        return NodeType.RETURN_STATEMENT;
    }
}
