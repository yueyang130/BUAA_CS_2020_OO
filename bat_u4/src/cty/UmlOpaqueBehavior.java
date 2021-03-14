import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlOpaqueBehavior extends UmlElement {
    private Visibility visibility;

    public UmlOpaqueBehavior(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlOpaqueBehavior e =
                (com.oocourse.uml3.models.elements.UmlOpaqueBehavior) element;
        visibility = e.getVisibility();
    }

    public ElementType getType() { return ElementType.UML_OPAQUE_BEHAVIOR; }

    public Visibility getVisibility() { return visibility; }
}
