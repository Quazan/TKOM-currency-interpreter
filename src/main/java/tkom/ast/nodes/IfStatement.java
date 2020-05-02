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

        //TODO that's a piece of ugly code
        Value cond = condition.evaluate(environment);
        switch (cond.getType()) {
            case INT:
                if (((IntNode) cond).getValue() != 0) {
                    trueBlock.execute(environment);
                } else {
                    falseBlock.execute(environment);
                }
                break;
            case DOUBLE:
                if (((DoubleNode) cond).getValue() != 0) {
                    trueBlock.execute(environment);
                } else {
                    falseBlock.execute(environment);
                }
                break;
            case CURRENCY:
                if (!((Currency) cond).getValue().equals(BigDecimal.ZERO)) {
                    trueBlock.execute(environment);
                } else {
                    falseBlock.execute(environment);
                }
                break;
            default:
                throw new RuntimeEnvironmentException();
        }

        environment.destroyScope();
        return new IntNode(0);
    }
}
