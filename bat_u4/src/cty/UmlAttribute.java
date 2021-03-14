import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlAttribute extends UmlElement {
    private Visibility visibility;
    private NameableType type;

    public UmlAttribute(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlAttribute e =
                (com.oocourse.uml3.models.elements.UmlAttribute) element;
        this.visibility = e.getVisibility();
        type = e.getType();
    }

    public ElementType getType() { return ElementType.UML_ATTRIBUTE; }

    public Visibility getVisibility() {
        return visibility;
    }

    public NameableType getAttributeType() { return type; }
}
