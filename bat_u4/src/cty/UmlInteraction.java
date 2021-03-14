import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

public class UmlInteraction extends UmlElement {
    private Visibility visibility;
    private int messageCount = 0;
    private MultiMap<String, UmlLifeline> objects;

    public UmlInteraction(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlInteraction e =
                (com.oocourse.uml3.models.elements.UmlInteraction) element;
        visibility = e.getVisibility();
        objects = new MultiMap<>();
    }

    public ElementType getType() { return ElementType.UML_INTERACTION; }

    public Visibility getVisibility() { return visibility; }

    public void addMessage() { messageCount += 1; }

    public void addObject(UmlLifeline obj) {
        objects.put(obj.getName(), obj);
    }

    public int queryObjectCount() { return objects.realSize(); }

    public int queryMessageCount() { return messageCount; }

    public int queryIncomingCount(String name) throws
            LifelineDuplicatedException, LifelineNotFoundException {
        int sz = objects.keySize(name);
        if (sz == 0) { throw new LifelineNotFoundException(getName(), name); }
        if (sz != 1) { throw new LifelineDuplicatedException(getName(), name); }
        UmlLifeline lifeline = objects.get(name);
        return lifeline.getIncomingCount();
    }
}
