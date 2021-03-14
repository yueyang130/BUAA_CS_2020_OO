package myelements.statechart;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import myelements.MyUmlElement;

import java.util.HashMap;

public class MyUmlStateMachine extends MyUmlElement {
    private MyUmlRegion region;
    
    public MyUmlStateMachine(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
        ElementType elementType = myUmlElement.getElementType();
        switch (elementType) {
            case UML_REGION:
                region = (MyUmlRegion) myUmlElement;
                break;
            default:
        }
    }
    
    public int getStateCount() {
        if (region == null) {
            return 0;
        }
        return region.getStateCount();
    }
    
    public int getTransitionCount() {
        if (region == null) {
            return 0;
        }
        return region.getTransitionCount();
    }
    
    public int getSubsequentStateCount(
            String stateMachineName, String stateName, HashMap<String,MyUmlElement> elementsIdMap)
            throws StateDuplicatedException, StateNotFoundException {
        if (region == null) {
            return 0;
        }
        return region.getSubsequentStateCount(stateMachineName, stateName, elementsIdMap);
    }
}
