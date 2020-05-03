package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.utils.NodeType;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class BoolNode implements Value {

    private boolean value;

    public BoolNode(boolean value) {
        this.value = value;
    }

    public BoolNode(Value from) {
        switch (from.getType()) {
            case CURRENCY:
                this.value = !((Currency) from).getValue().equals(BigDecimal.ZERO);
                break;
            case DOUBLE:
                this.value = ((DoubleNode) from).getValue() != 0;
                break;
            case INT:
                this.value = ((IntNode) from).getValue() != 0;
                break;
            case BOOL:
                this.value = ((BoolNode) from).isValue();
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.BOOL;
    }

}
