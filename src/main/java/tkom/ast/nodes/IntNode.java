package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Value;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class IntNode implements Expression, Value {

    private int value;

    public IntNode(int value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }

    public void add(IntNode rightOperand) {
        value += rightOperand.getValue();
    }

    public void subtract(IntNode rightOperand) {
        value -= rightOperand.getValue();
    }

    public void multiply(IntNode rightOperand) {
        value *= rightOperand.getValue();
    }

    public void divide(IntNode rightOperand) {
        value /= rightOperand.getValue();
    }
}
