package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class ReturnStatement implements Statement{

    private ExpressionNode expressionNode;

    public ReturnStatement(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    @Override
    public NodeType getType() {
        return NodeType.RETURN_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        return expressionNode.evaluate(environment);
    }
}
