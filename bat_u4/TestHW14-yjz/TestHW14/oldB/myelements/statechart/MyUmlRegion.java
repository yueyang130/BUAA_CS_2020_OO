package myelements.statechart;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlRegion;
import myelements.MyUmlElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyUmlRegion extends MyUmlElement {
    private MyUmlPseudostate pseudostate;
    private MyUmlFinalState finalstate;
    private HashSet<String> states = new HashSet<String>();
    private HashSet<String> transitions = new HashSet<String>();
    private int stateCount = 0;
    
    public MyUmlRegion(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
        ElementType elementType = myUmlElement.getElementType();
        switch (elementType) {
            case UML_PSEUDOSTATE:
                pseudostate = (MyUmlPseudostate) myUmlElement;
                stateCount++;
                break;
            case UML_FINAL_STATE:
                finalstate = (MyUmlFinalState) myUmlElement;
                stateCount++;
                break;
            case UML_STATE:
                states.add(myUmlElement.getId());
                stateCount++;
                break;
            case UML_TRANSITION:
                transitions.add(myUmlElement.getId());
                break;
            default:
        }
    }
    
    public Visibility getVisibility() {
        return ((UmlRegion) super.getUmlElement()).getVisibility();
    }
    
    public int getStateCount() {
        return stateCount;
    }
    
    public int getTransitionCount() {
        return transitions.size();
    }
    
    public int getSubsequentStateCount(
            String stateMachineName, String stateName, HashMap<String,MyUmlElement> elementsIdMap)
            throws StateNotFoundException, StateDuplicatedException {
        int num = 0;
        String stateID = null;
        if (pseudostate != null && pseudostate.getName() != null
                && pseudostate.getName().equals(stateName)) {
            num++;
            stateID = pseudostate.getId();
        }
        if (finalstate != null && finalstate.getName() != null
                && finalstate.getName().equals(stateName)) {
            num++;
            stateID = finalstate.getId();
        }
        if (num > 1) {
            throw new StateDuplicatedException(stateMachineName,stateName);
        }
        for (Iterator<String> it = states.iterator(); it.hasNext();) {
            MyUmlElement myUmlElement = elementsIdMap.get(it.next());
            if (myUmlElement.getName() != null && myUmlElement.getName().equals(stateName)) {
                num++;
                stateID = myUmlElement.getId();
            }
            if (num > 1) {
                throw new StateDuplicatedException(stateMachineName,stateName);
            }
        }
        
        if (num == 0) {
            throw new StateNotFoundException(stateMachineName,stateName);
        }
        return dfs(stateID,new HashSet<String>(),elementsIdMap);
    }
    
    private int dfs(
            String stateID, HashSet<String> visited, HashMap<String,MyUmlElement> elementsIdMap) {
        int res = 0;
        for (Iterator<String> it = transitions.iterator();it.hasNext();) {
            MyUmlTransition myUmlTransition = (MyUmlTransition) elementsIdMap.get(it.next());
            String source = myUmlTransition.getSource();
            if (source != null && source.equals(stateID)) {
                String target = myUmlTransition.getTarget();
                if (target != null && !visited.contains(target)) {
                    visited.add(target);
                    res++;
                    res = res + dfs(target,visited,elementsIdMap);
                }
            }
        }
        return res;
    }
}
