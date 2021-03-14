import com.oocourse.uml3.models.common.Aggregation;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlAssociationEnd extends UmlElement {
    private Aggregation aggregation;
    private String multiplicity;
    private String referenceid;
    private Visibility visibility;
    private UmlExtendable reference;

    public UmlAssociationEnd(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlAssociationEnd e =
                (com.oocourse.uml3.models.elements.UmlAssociationEnd) element;
        aggregation = e.getAggregation();
        multiplicity = e.getMultiplicity();
        referenceid = e.getReference();
        visibility = e.getVisibility();
    }

    public ElementType getType() { return ElementType.UML_ASSOCIATION_END; }

    public Aggregation getAggregation() { return aggregation; }

    public String getMultiplicity() {
        return multiplicity;
    }

    public String getReferenceId() {
        return referenceid;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public UmlExtendable getReference() { return reference; }

    public void setReference(UmlExtendable cls) {
        this.reference = cls;
        this.referenceid = cls.getId();
    }
}
