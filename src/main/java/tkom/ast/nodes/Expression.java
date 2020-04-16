package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.utils.TokenType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Expression implements Node {

    private List<TokenType> operations;
    private List<Node> operands;

    public Expression() {
        this.operations = new ArrayList<>();
        this.operands = new ArrayList<>();
    }

    public void addOperation(TokenType operationType) {
        operations.add(operationType);
    }

    public void addOperand(Node operand) {
        operands.add(operand);
    }

    @Override
    public NodeType getType() {
        return NodeType.EXPRESSION;
    }
}
