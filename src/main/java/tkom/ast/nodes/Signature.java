package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Signature {
    private String returnType;

    private String name;
}
