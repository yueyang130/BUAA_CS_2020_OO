import com.oocourse.uml3.models.common.ElementType;

public class UmlEndpoint extends UmlInteractionObject {
    public UmlEndpoint(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlEndpoint e =
                (com.oocourse.uml3.models.elements.UmlEndpoint) element;
        setVisibility(e.getVisibility());
    }

    public ElementType getType() { return ElementType.UML_ENDPOINT; }
}
