package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
public class StringNode implements Expression, Value {

    private final String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.STRING;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public BoolNode isEqual(Value rightOperand) throws RuntimeEnvironmentException {
        if (rightOperand.getType() == NodeType.STRING) {
            return new BoolNode(value.equals(((StringNode) rightOperand).value));
        }

        throw new RuntimeEnvironmentException("Cannot compare: " + rightOperand.getType() +
                " to " + getType());
    }
}
