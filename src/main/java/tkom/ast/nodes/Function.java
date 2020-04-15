package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.ast.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Function extends Signature implements Node {

    private List<Signature> parameters;

    private StatementBlock statementBlock;

    public Function() {
        this.parameters = new ArrayList<>();
    }

    public void addParameter(Signature parameter) {
        parameters.add(parameter);
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION;
    }

}
