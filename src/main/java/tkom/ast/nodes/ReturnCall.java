package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class ReturnCall implements Value {

    private Value returnedValue;

    public ReturnCall(Value returnedValue) {
        this.returnedValue = returnedValue;
    }

    @Override
    public NodeType getType() {
        return NodeType.RETURN_CALL;
    }
}
