import com.oocourse.uml3.models.common.ElementType;

public class UmlInterfaceRealization extends UmlElement {
    private String sourceid;
    private String targetid;
    private UmlClass source;
    private UmlInterface target;

    public UmlInterfaceRealization(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlInterfaceRealization e =
                (com.oocourse.uml3.models.elements.UmlInterfaceRealization)
                        element;
        this.sourceid = e.getSource();
        this.targetid = e.getTarget();
    }

    public ElementType getType() {
        return ElementType.UML_INTERFACE_REALIZATION; }

    public String getSourceId() { return sourceid; }

    public String getTargetid() { return targetid; }

    public void setSource(UmlClass source) {
        this.source = source;
        this.sourceid = source.getId();
    }

    public void setTarget(UmlInterface target) {
        this.target = target;
        this.targetid = target.getId();
    }

    public String getInterfaceName() { return target.getName(); }

    public String getClassName() { return source.getName(); }
}
