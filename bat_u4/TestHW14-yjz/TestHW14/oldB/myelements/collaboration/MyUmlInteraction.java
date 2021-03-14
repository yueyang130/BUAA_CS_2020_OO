package myelements.collaboration;

import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInteraction;
import myelements.MyUmlElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyUmlInteraction extends MyUmlElement {
    private HashSet<String> lifelines = new HashSet<String>();
    private HashSet<String> messages = new HashSet<String>();
    private HashSet<String> endpoints = new HashSet<String>();
    
    public MyUmlInteraction(UmlElement umlElement) {
        super(umlElement);
    }
    
    public Visibility getVisibility() {
        return ((UmlInteraction) super.getUmlElement()).getVisibility();
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
        ElementType elementType = myUmlElement.getElementType();
        switch (elementType) {
            case UML_LIFELINE:
                lifelines.add(myUmlElement.getId());
                break;
            case UML_MESSAGE:
                messages.add(myUmlElement.getId());
                break;
            case UML_ENDPOINT:
                endpoints.add(myUmlElement.getId());
                break;
            default:
        }
    }
    
    public int getParticipantCount() {
        return lifelines.size();
    }
    
    public int getMessageCount() {
        return messages.size();
    }
    
    public int getIncomingMessageCount(
            String interactionName, String lifelineName, HashMap<String,MyUmlElement> elementsIdMap)
            throws LifelineDuplicatedException, LifelineNotFoundException {
        int num = 0;
        String liflineID = null;
        for (Iterator<String> it = lifelines.iterator();it.hasNext();) {
            MyUmlElement myUmlElement = elementsIdMap.get(it.next());
            if (myUmlElement.getName() != null && myUmlElement.getName().equals(lifelineName)) {
                num++;
                liflineID = myUmlElement.getId();
            }
            if (num > 1) {
                throw new LifelineDuplicatedException(interactionName,lifelineName);
            }
        }
    
        if (num == 0) {
            throw new LifelineNotFoundException(interactionName,lifelineName);
        }
        int res = 0;
        for (Iterator<String> it = messages.iterator();it.hasNext();) {
            MyUmlMessage myUmlMessage = (MyUmlMessage) elementsIdMap.get(it.next());
            String target = myUmlMessage.getTarget();
            if (target != null && target.equals(liflineID)) {
                res++;
            }
        }
        return res;
    }
}
