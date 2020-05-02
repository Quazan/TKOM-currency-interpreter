package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class FunctionCall implements Node, Statement, Expression {

    private String identifier;
    List<ExpressionNode> arguments;

    public FunctionCall() {
        this.arguments = new ArrayList<>();
    }

    public FunctionCall(String identifier) {
        this.identifier = identifier;
        this.arguments = new ArrayList<>();
    }

    public void addArgument(ExpressionNode expressionNode) {
        arguments.add(expressionNode);
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION_CALL;
    }
}
