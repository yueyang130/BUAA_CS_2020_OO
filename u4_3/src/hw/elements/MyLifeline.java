package hw.elements;

import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.List;

public class MyLifeline {
    private UmlLifeline umlLifeline;
    private List<UmlMessage> incomingMessages = new ArrayList<>();

    public MyLifeline(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }

    public String getName() {
        return umlLifeline.getName();
    }

    public void addIncomingMessage(UmlMessage umlMessage) {
        incomingMessages.add(umlMessage);
    }

    public int getIncomingMesgNum() {
        return incomingMessages.size();
    }
}
