package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.utils.TokenType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Condition implements Node, Expression {

    boolean negated = false;

    private TokenType operator;

    private List<Expression> operands;

    public Condition() {
        this.operands = new ArrayList<>();
    }

    public void addOperand(Expression operand) {
        this.operands.add(operand);
    }

    public boolean isNegated() {
        return negated;
    }

    @Override
    public NodeType getType() {
        return NodeType.CONDITION;
    }
}
