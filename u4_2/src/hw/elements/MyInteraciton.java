package hw.elements;

import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyInteraciton {
    private UmlInteraction umlInteraction;
    private int participantNum = 0;
    private Map<String, List<MyLifeline>> nameLifelineMap = new HashMap<>();
    private List<UmlMessage> messeges = new ArrayList<>();

    public MyInteraciton(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }

    public String getName() { return umlInteraction.getName(); }

    public void addParticipant(MyLifeline myLifeline) {
        participantNum += 1;
        if (nameLifelineMap.containsKey(myLifeline.getName())) {
            nameLifelineMap.get(myLifeline.getName()).add(myLifeline);
        } else {
            nameLifelineMap.put(myLifeline.getName(), new ArrayList<MyLifeline>() {
                {
                    add(myLifeline);
                }
            });
        }
    }

    public void addMessage(UmlMessage umlMsg, Object target) {
        if (target instanceof MyLifeline) {
            ((MyLifeline) target).addIncomingMessage(umlMsg);
        }
        messeges.add(umlMsg);
    }

    public int getMsgNum() { return messeges.size(); }

    public int getParticipantNum() { return participantNum; }

    public int getIncomingMsgCnt(String lifelineName)
            throws LifelineNotFoundException, LifelineDuplicatedException  {
        if (!nameLifelineMap.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(getName(), lifelineName);
        }
        if (nameLifelineMap.get(lifelineName).size() > 1) {
            throw new LifelineDuplicatedException(getName(), lifelineName);
        }
        return nameLifelineMap.get(lifelineName).get(0).getIncomingMesgNum();
    }

}
