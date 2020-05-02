package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Signature {
    private String returnType;

    private String identifier;

    public Signature(String returnType, String identifier) {
        this.returnType = returnType;
        this.identifier = identifier;
    }
}
