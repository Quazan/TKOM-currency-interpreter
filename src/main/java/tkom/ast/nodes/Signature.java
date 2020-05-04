package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


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
