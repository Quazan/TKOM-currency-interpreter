package tkom.ast.nodes;

import lombok.Getter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;

@Getter
@ToString
public class ExecuteOut {

    private final Value returnedValue;

    private final ExecuteStatus status;

    public ExecuteOut(ExecuteStatus status, Value returnedValue) {
        this.status = status;
        this.returnedValue = returnedValue;
    }

    public ExecuteOut(ExecuteStatus status) {
        this.status = status;
        this.returnedValue = null;
    }

    public boolean isReturnCall() {
        return status == ExecuteStatus.RETURN;
    }
}
