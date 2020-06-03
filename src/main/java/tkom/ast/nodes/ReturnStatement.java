package tkom.ast.nodes;

import lombok.Getter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Statement;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;

@Getter
@ToString
public class ReturnStatement implements Statement {

    private final Expression expressionNode;

    public ReturnStatement(Expression expression) {
        this.expressionNode = expression;
    }

    @Override
    public NodeType getType() {
        return NodeType.RETURN_STATEMENT;
    }

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        return new ExecuteOut(ExecuteStatus.RETURN, expressionNode.evaluate(environment));
    }

}
