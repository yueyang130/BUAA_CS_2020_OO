package hw.elements;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.elements.UmlStateMachine;

public class MyStateMachine {
    private UmlStateMachine umlStateMachine;
    private MyRegion region;

    public MyStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }

    public String getName() {
        return umlStateMachine.getName();
    }

    public void setRegion(MyRegion myRegion) {
        region = myRegion;
    }

    public int getTranslationNum() {
        return region.getTranslationNum();
    }

    public int getStateNum() {
        return region.getStateNum();
    }

    public int getSubsequentStateNum(String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        return region.getSubsequentStateNum(stateName);
    }

}
