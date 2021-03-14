import com.oocourse.uml3.models.common.ElementType;

public class UmlGeneralization extends UmlElement {
    private String sourceid;
    private String targetid;
    private UmlClass source;
    private UmlClass target;

    public UmlGeneralization(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlGeneralization e =
                (com.oocourse.uml3.models.elements.UmlGeneralization) element;
        sourceid = e.getSource();
        targetid = e.getTarget();
    }

    public ElementType getType() { return ElementType.UML_GENERALIZATION; }

    public String getSourceId() { return sourceid; }

    public String getTargetid() { return targetid; }

    public void setSource(UmlClass source) {
        this.source = source;
        this.sourceid = source.getId();
    }

    public void setTarget(UmlClass target) {
        this.target = target;
        this.targetid = target.getId();
    }

    public String getParentName() { return target.getName(); }

    public String getChildName() { return source.getName(); }
}
