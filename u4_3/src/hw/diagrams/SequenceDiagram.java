package hw.diagrams;

import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import hw.elements.MyInteraciton;
import hw.elements.MyLifeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequenceDiagram {
    private static final int MapCapacity = 256;
    private Map<String, List<MyInteraciton>> nameInteractionMap = new HashMap<>(MapCapacity);
    private Map<String, Object> id2elem;

    public SequenceDiagram(Map<String, Object> id2elem) {
        this.id2elem = id2elem;
    }

    public void parseUmlInteraction(UmlInteraction umlInteraction) {
        MyInteraciton myInteraciton = new MyInteraciton(umlInteraction);
        id2elem.put(umlInteraction.getId(), myInteraciton);
        if (nameInteractionMap.containsKey(umlInteraction.getName())) {
            nameInteractionMap.get(umlInteraction.getName()).add(myInteraciton);
        } else {
            nameInteractionMap.put(umlInteraction.getName(), new ArrayList<MyInteraciton>() {
                {
                    add(myInteraciton);
                }
            });
        }
    }

    public void parseUmlLifeline(UmlLifeline umlLifeline) {
        MyLifeline myLifeline = new MyLifeline(umlLifeline);
        id2elem.put(umlLifeline.getId(), myLifeline);
        MyInteraciton myInteraciton = (MyInteraciton) id2elem.get(umlLifeline.getParentId());
        myInteraciton.addParticipant(myLifeline);

    }

    public void parseUmlMessage(UmlMessage umlMsg) {
        // umlMsg的source和target也可能是UMLEndPoint
        Object obj = id2elem.get(umlMsg.getTarget());
        MyInteraciton myInteraciton = (MyInteraciton) id2elem.get(umlMsg.getParentId());
        myInteraciton.addMessage(umlMsg, obj);

    }

    private MyInteraciton checkInteractionName(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {

        List<MyInteraciton> interacitonList = nameInteractionMap.get(interactionName);
        if (interacitonList == null) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (interacitonList.size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return interacitonList.get(0);
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraciton myInteraciton = checkInteractionName(interactionName);
        return myInteraciton.getParticipantNum();
    }

    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraciton myInteraciton = checkInteractionName(interactionName);
        return myInteraciton.getMsgNum();

    }

    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyInteraciton myInteraciton = checkInteractionName(interactionName);
        return myInteraciton.getIncomingMsgCnt(lifelineName);
    }

}
