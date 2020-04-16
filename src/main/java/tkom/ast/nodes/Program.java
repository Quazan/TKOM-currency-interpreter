package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.Node;
import tkom.utils.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Program implements Node {
    private final List<Function> functions;

    public Program() {
        this.functions = new ArrayList<>();
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    @Override
    public NodeType getType() {
        return NodeType.PROGRAM;
    }
}
