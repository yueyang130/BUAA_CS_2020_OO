import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.common.Visibility;

public class UmlMessage extends UmlElement {
    private MessageSort messageSort;
    private Visibility visibility;
    private String sourceId;
    private String targetId;
    private UmlInteractionObject source;
    private UmlInteractionObject target;

    public UmlMessage(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlMessage e =
                (com.oocourse.uml3.models.elements.UmlMessage) element;
        messageSort = e.getMessageSort();
        visibility = e.getVisibility();
        sourceId = e.getSource();
        targetId = e.getTarget();
    }

    public ElementType getType() { return ElementType.UML_MESSAGE; }

    public MessageSort getMessageSort() { return messageSort; }

    public Visibility getVisibility() { return visibility; }

    public String getSourceId() { return sourceId; }

    public String getTargetId() { return targetId; }

    public void setSource(UmlInteractionObject e) { source = e; }

    public void setTarget(UmlInteractionObject e) { target = e; }

    public UmlInteractionObject getSource() { return source; }

    public UmlInteractionObject getTarget() { return target; }
}
