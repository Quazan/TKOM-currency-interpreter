package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class FunctionCall implements Node, Statement {

    private String name;
    List<Expression> arguments;

    public FunctionCall() {
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression expression) {
        arguments.add(expression);
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION_CALL;
    }
}
