package tkom.ast.nodes;

import lombok.Getter;
import lombok.ToString;
import tkom.utils.NodeType;


@Getter
@ToString
public class Signature {
    private final String returnType;

    private final String identifier;

    public Signature(String returnType, String identifier) {
        this.returnType = returnType;
        this.identifier = identifier;
    }
}
