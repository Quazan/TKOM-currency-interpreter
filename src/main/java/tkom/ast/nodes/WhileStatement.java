package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.ast.Statement;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class WhileStatement implements Statement {

    private Condition condition;

    private StatementBlock whileBlock;

    @Override
    public NodeType getType() {
        return NodeType.WHILE_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws RuntimeEnvironmentException {
        Value ret = new IntNode(0);

        while (((BoolNode) condition.evaluate(environment)).isValue()) {
            environment.createNewLocalScope();
            ret = whileBlock.execute(environment);
            if (ret.getType() == NodeType.RETURN_CALL) {
                environment.destroyScope();
                return ret;
            }
            environment.destroyScope();
        }

        return ret;
    }


}
