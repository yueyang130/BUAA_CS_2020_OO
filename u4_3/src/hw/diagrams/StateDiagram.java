package hw.diagrams;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;
import hw.elements.MyRegion;
import hw.elements.MyState;
import hw.elements.MyStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateDiagram {
    private static final int MapCapacity = 256;
    private Map<String, List<MyStateMachine>> nameSmMap = new HashMap<>(MapCapacity);
    private List<MyStateMachine> smList = new ArrayList<>(MapCapacity);
    private Map<String, Object> id2elem;

    public StateDiagram(Map<String, Object> id2elem) {
        this.id2elem = id2elem;
    }

    public void parseUmlStateMachine(UmlStateMachine sm) {
        MyStateMachine mySm = new MyStateMachine(sm);
        id2elem.put(sm.getId(), mySm);
        if (nameSmMap.containsKey(sm.getName())) {
            nameSmMap.get(sm.getName()).add(mySm);
        } else {
            List<MyStateMachine> ls = new ArrayList<>();
            ls.add(mySm);
            nameSmMap.put(sm.getName(), ls);
        }
        smList.add(mySm);
    }

    public void parseUmlRegion(UmlRegion umlRegion) {
        MyStateMachine sm = (MyStateMachine) id2elem.get(umlRegion.getParentId());
        MyRegion myRegion = new MyRegion(umlRegion);
        id2elem.put(umlRegion.getId(), myRegion);
        sm.setRegion(myRegion);
        myRegion.setParent(sm);
    }

    public void parseState(UmlElement state) {
        MyRegion myRegion = (MyRegion) id2elem.get(state.getParentId());
        MyState myState = new MyState(state);
        id2elem.put(state.getId(), myState);
        myRegion.addState(myState);
    }

    public void parseUmlTranslation(UmlTransition umlTrans) {
        String sourceId = umlTrans.getSource();
        String targetId = umlTrans.getTarget();
        MyState sourceState = (MyState) id2elem.get(sourceId);
        MyState targrtState = (MyState) id2elem.get(targetId);
        MyRegion myRegion   = (MyRegion) id2elem.get(umlTrans.getParentId());
        myRegion.addTranslation(umlTrans, sourceState, targrtState);
    }

    private MyStateMachine checkSmName(String smName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        List<MyStateMachine> smLs = nameSmMap.get(smName);
        if (smLs == null) {
            throw new StateMachineNotFoundException(smName);
        }
        if (smLs.size() > 1) {
            throw new StateMachineDuplicatedException(smName);
        }
        return smLs.get(0);
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyStateMachine mySm = checkSmName(stateMachineName);
        return mySm.getStateNum();
    }

    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyStateMachine mySm = checkSmName(stateMachineName);
        return mySm.getTranslationNum();
    }

    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyStateMachine mySm = checkSmName(stateMachineName);
        return mySm.getSubsequentStateNum(stateName);
    }

    public void checkForUml007() throws UmlRule007Exception {
        for (MyStateMachine sm : smList) {
            sm.checkFinalState();
        }
    }

    public void checkForUml008() throws UmlRule008Exception {
        for (MyStateMachine sm : smList) {
            sm.checkInitState();
        }
    }

}
