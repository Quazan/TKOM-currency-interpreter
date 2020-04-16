package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Getter
@Setter
public class MyString implements Node {

    private String value;

    public MyString(String value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return null;
    }
}
