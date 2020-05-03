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

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class IfStatement implements Statement {

    private Condition condition;

    private StatementBlock trueBlock;

    private StatementBlock falseBlock;

    @Override
    public NodeType getType() {
        return NodeType.IF_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        environment.createNewLocalScope();

        if(Value.getBoolValue(condition.evaluate(environment))) {
            trueBlock.execute(environment);
        } else if (falseBlock != null) {
            falseBlock.execute(environment);
        }

        environment.destroyScope();
        return new IntNode(0);
    }
}
