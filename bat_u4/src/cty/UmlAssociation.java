import com.oocourse.uml3.models.common.ElementType;

public class UmlAssociation extends UmlElement {
    private String endid1;
    private String endid2;
    private UmlAssociationEnd end1;
    private UmlAssociationEnd end2;

    public UmlAssociation(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlAssociation e =
                (com.oocourse.uml3.models.elements.UmlAssociation) element;
        this.endid1 = e.getEnd1();
        this.endid2 = e.getEnd2();
    }

    public ElementType getType() { return ElementType.UML_ASSOCIATION; }

    public String getEndId1() { return endid1; }

    public String getEndId2() { return endid2; }

    public void setEnd1(UmlAssociationEnd end) {
        this.end1 = end;
        this.endid1 = end.getId();
    }

    public void setEnd2(UmlAssociationEnd end) {
        this.end2 = end;
        this.endid2 = end.getId();
    }

    public UmlAssociationEnd getEnd1() { return end1; }

    public UmlAssociationEnd getEnd2() { return end2; }

    public UmlDirectedAssociation getDirected1() {
        return new UmlDirectedAssociation(getId(), end2.getName(),
                end1.getReference(), end2.getReference());
    }

    public UmlDirectedAssociation getDirected2() {
        return new UmlDirectedAssociation(getId(), end1.getName(),
                end2.getReference(), end1.getReference());
    }

}
