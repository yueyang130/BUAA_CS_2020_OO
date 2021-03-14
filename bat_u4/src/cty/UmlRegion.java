import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlRegion extends UmlElement {
    private Visibility visibility;

    public UmlRegion(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlRegion e =
                (com.oocourse.uml3.models.elements.UmlRegion) element;
        visibility = e.getVisibility();
    }

    public ElementType getType() { return ElementType.UML_REGION; }

    public Visibility getVisibility() { return visibility; }
}
