package tkom.ast.nodes;

import lombok.Getter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.utils.NodeType;

@Getter
@ToString
public class ReturnCall implements Value {

    private final Value returnedValue;

    public ReturnCall(Value returnedValue) {
        this.returnedValue = returnedValue;
    }

    @Override
    public NodeType getType() {
        return NodeType.RETURN_CALL;
    }
}
