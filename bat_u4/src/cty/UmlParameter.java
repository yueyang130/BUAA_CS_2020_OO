import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.NameableType;

public class UmlParameter extends UmlElement {
    private Direction direction;
    private NameableType type;

    public UmlParameter(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlParameter e =
                (com.oocourse.uml3.models.elements.UmlParameter) element;
        direction = e.getDirection();
        type = e.getType();
    }

    public boolean isNameEmpty() {
        return direction != Direction.RETURN && getName() == null;
    }

    public ElementType getType() { return ElementType.UML_PARAMETER; }

    public Direction getDirection() { return direction; }

    public NameableType getParameterType() { return type; }

    public String toString() {
        if (getName() == null) {
            return type.toJsonString();
        }
        return direction.toString().toLowerCase() + " "
                + getName() + ":" + type.toJsonString();
    }
}
