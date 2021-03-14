import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlEvent extends UmlElement {
    private Visibility visibility;
    private String expression;
    private String value;

    public UmlEvent(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlEvent e =
                (com.oocourse.uml3.models.elements.UmlEvent) element;
        visibility = e.getVisibility();
        expression = e.getExpression();
        value = e.getValue();
    }

    public ElementType getType() { return ElementType.UML_EVENT; }

    public Visibility getVisibility() { return visibility; }

    public String getExpression() { return expression; }

    public String getValue() { return value; }
}
