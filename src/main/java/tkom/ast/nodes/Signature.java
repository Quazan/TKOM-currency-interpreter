package tkom.ast.nodes;

import lombok.Data;
import tkom.utils.Type;

@Data
public class Signature {
    private Type returnType;

    private String name;
}
