import com.oocourse.uml3.models.common.ElementType;

public class UmlDirectedAssociation extends UmlElement {
    private UmlExtendable source;
    private UmlExtendable target;

    public UmlDirectedAssociation(String id, String name,
            UmlExtendable source,
            UmlExtendable target) {
        super(id);
        this.source = source;
        this.target = target;
        setName(name);
    }

    public UmlExtendable getSource() {
        return source;
    }

    public UmlExtendable getTarget() {
        return target;
    }

    public String getSourceId() {
        return source.getId();
    }

    public String getTargetId() {
        return target.getId();
    }

    public String getSourceName() {
        return source.getName();
    }

    public String getTargetName() {
        return target.getName();
    }

    public boolean isClassAssociation() {
        return source.getType() == ElementType.UML_CLASS &&
                target.getType() == ElementType.UML_CLASS;
    }

    @Override
    public ElementType getType() {
        return null;
    }

    @Override
    public String toString() {
        return "{" + source.getName() + " -> " + target.getName() + "}";
    }
}
