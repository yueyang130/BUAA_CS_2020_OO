import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlOperation extends UmlElement {
    private Visibility visibility;
    private MultiMap<String, UmlParameter> parameters;
    private boolean nonReturn = true;
    private boolean nonParam = true;

    public UmlOperation(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlOperation e =
                (com.oocourse.uml3.models.elements.UmlOperation) element;
        visibility = e.getVisibility();
        parameters = new MultiMap<>();
    }

    public ElementType getType() { return ElementType.UML_OPERATION; }

    public void addParameter(UmlParameter parameter) {
        parameters.put(parameter.getName(), parameter);
        if (parameter.getDirection() == Direction.RETURN) {
            nonReturn = false;
        } else {
            nonParam = false;
        }
    }

    public boolean isNonReturn() {
        return nonReturn;
    }

    public boolean isNonParam() {
        return nonParam;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        StringBuffer bufreturn = new StringBuffer();
        for (UmlParameter parameter: parameters.values()) {
            if (parameter.getDirection() == Direction.RETURN) {
                if (bufreturn.length() != 0) {
                    bufreturn.append(", ");
                }
                bufreturn.append(parameter.toString());
            } else {
                if (buf.length() != 0) {
                    buf.append(", ");
                }
                buf.append(parameter.toString());
            }
        }
        StringBuffer res = new StringBuffer();
        if (getName() != null) {
            res.append(getName());
        }
        res.append('(');
        res.append(buf);
        res.append(')');
        if (bufreturn.length() != 0) {
            res.append(": ");
            res.append(bufreturn);
        }
        return res.toString();
    }
}
