import com.oocourse.uml3.models.common.ElementType;

public class UmlLifeline extends UmlInteractionObject {
    private boolean multiInstance;
    private String representId;
    private UmlAttribute represent;

    public UmlLifeline(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlLifeline e =
                (com.oocourse.uml3.models.elements.UmlLifeline) element;
        setVisibility(e.getVisibility());
        multiInstance = e.isMultiInstance();
        representId = e.getRepresent();
    }

    public ElementType getType() { return ElementType.UML_LIFELINE; }

    public boolean isMultiInstance() { return multiInstance; }

    public String getRepresentId() { return representId; }

    public UmlAttribute getRepresent() { return represent; }

    public void setRepresent(UmlAttribute attr) { represent = attr; }
}
