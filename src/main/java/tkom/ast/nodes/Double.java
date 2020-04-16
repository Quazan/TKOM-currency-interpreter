package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class Double implements Node {

    private double value;

    @Override
    public NodeType getType() {
        return null;
    }
}
