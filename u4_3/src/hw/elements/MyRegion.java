package hw.elements;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MyRegion {
    private UmlRegion umlRegion;
    private MyStateMachine parent;
    private Map<String, List<MyState>> nameStateMap = new HashMap<>();
    // states includes umlstate, umlPseudoState, umlFinalState
    private MyState initState = null;
    private MyState finalState = null;
    private List<MyState> states = new ArrayList<>();
    private List<UmlTransition> transitions = new ArrayList<>();

    public MyRegion(UmlRegion umlRegion) {
        this.umlRegion = umlRegion;
    }

    public void addState(MyState myState) {

        if (myState.getStateType() == ElementType.UML_STATE) {
            states.add(myState);
            if (nameStateMap.containsKey(myState.getName())) {
                nameStateMap.get(myState.getName()).add(myState);
            } else {
                List<MyState> ls = new ArrayList<>();
                ls.add(myState);
                nameStateMap.put(myState.getName(), ls);
            }

        } else if (myState.getStateType() == ElementType.UML_FINAL_STATE) {
            states.add(myState);
            finalState = myState;
        } else if (myState.getStateType() == ElementType.UML_PSEUDOSTATE) {
            states.add(myState);
            initState = myState;
        }
    }

    public void setParent(MyStateMachine stateMachine) {
        this.parent = stateMachine;
    }

    public void addTranslation(UmlTransition umlTransition, MyState source, MyState target) {
        transitions.add(umlTransition);
        source.addSubsequentState(target);
    }

    public int getTranslationNum() {
        return transitions.size();
    }

    public int getStateNum() {
        return states.size();
    }

    public int getSubsequentStateNum(String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        List<MyState> stateLs = nameStateMap.get(stateName);
        if (stateLs == null) {
            throw new StateNotFoundException(parent.getName(), stateName);
        }
        if (stateLs.size() > 1) {
            throw new StateDuplicatedException(parent.getName(), stateName);
        }
        Set<MyState> subsequentSet = new HashSet<>();
        Queue<MyState> queue = new LinkedList<>(stateLs.get(0).getLinkedStates());
        while (!queue.isEmpty()) {
            MyState current = queue.poll();
            if (!subsequentSet.contains(current)) {
                subsequentSet.add(current);
                queue.addAll(current.getLinkedStates());
            }
        }
        return subsequentSet.size();
    }

    public void checkFinalState() throws UmlRule007Exception {
        if (finalState != null) {
            if (finalState.getTranslationNum() != 0) {
                throw new UmlRule007Exception();
            }
        }
    }

    public void checkInitState() throws UmlRule008Exception {
        // 多个迁移，同一尾端，仍然算多个迁出
        if (initState != null) {
            if (initState.getTranslationNum() > 1) {
                throw new UmlRule008Exception();
            }
        }
    }

}
