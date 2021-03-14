import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlTransition extends UmlElement {
    private String guard;
    private String sourceId;
    private String targetId;
    private Visibility visibility;
    private UmlState source;
    private UmlState target;

    public UmlTransition(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlTransition e =
                (com.oocourse.uml3.models.elements.UmlTransition) element;
        guard = e.getGuard();
        sourceId = e.getSource();
        targetId = e.getTarget();
        visibility = e.getVisibility();
    }

    public ElementType getType() { return ElementType.UML_TRANSITION; }

    public Visibility getVisibility() { return visibility; }

    public String getSourceId() { return sourceId; }

    public String getTargetId() { return targetId; }

    public UmlState getSource() { return source; }

    public UmlState getTarget() { return target; }

    public void setSource(UmlState state) {
        source = state;
    }

    public void setTarget(UmlState state) {
        target = state;
    }
}
