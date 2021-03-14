import com.oocourse.uml3.models.common.Visibility;

public abstract class UmlInteractionObject extends UmlElement {
    private MultiMap<String, UmlMessage> incoming;
    private MultiMap<String, UmlMessage> sent;
    private Visibility visibility;

    public Visibility getVisibility() { return visibility; }

    protected void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public UmlInteractionObject(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        incoming = new MultiMap<>();
        sent = new MultiMap<>();
    }

    public void addIncoming(UmlMessage msg) {
        incoming.put(msg.getSourceId(), msg);
    }

    public void addSent(UmlMessage msg) {
        sent.put(msg.getTargetId(), msg);
    }

    public int getIncomingCount() { return incoming.realSize(); }

    public int getSentCount() { return sent.realSize(); }
}
