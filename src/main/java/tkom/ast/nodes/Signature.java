package tkom.ast.nodes;

import lombok.Data;
import tkom.utils.Type;

@Data
public abstract class Signature {
    private Type returnType;

    private String name;
}
