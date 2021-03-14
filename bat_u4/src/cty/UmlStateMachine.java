import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.models.common.ElementType;

import java.util.HashSet;
import java.util.LinkedList;

public class UmlStateMachine extends UmlElement {
    private MultiMap<String, UmlState> states;
    private int transitionCount = 0;

    public UmlStateMachine(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlStateMachine e =
                (com.oocourse.uml3.models.elements.UmlStateMachine) element;
        states = new MultiMap<>();
    }

    public ElementType getType() { return ElementType.UML_STATE_MACHINE; }

    public void addState(UmlState state) {
        states.put(state.getName(), state);
    }

    public int queryStateCount() {
        return states.realSize();
    }

    public void addTransition() { ++transitionCount; }

    public int queryTransitionCount() { return transitionCount; }

    private UmlState getState(String statename) throws
            StateDuplicatedException, StateNotFoundException {
        int sz = states.keySize(statename);
        if (sz == 0) { throw new StateNotFoundException(getName(), statename); }
        if (sz != 1) {
            throw new StateDuplicatedException(getName(), statename); }
        return states.get(statename);
    }

    public int querySubsequenceCount(String stateName) throws
            StateDuplicatedException, StateNotFoundException {
        UmlState state = getState(stateName);
        HashSet<String> visited = new HashSet<>();
        LinkedList<UmlState> queue = new LinkedList<>();
        queue.addLast(state);
        while (!queue.isEmpty()) {
            UmlState s = queue.removeFirst();
            for (UmlTransition t: s.getSubsequences()) {
                String id = t.getTargetId();
                if (!visited.contains(id)) {
                    UmlState target = t.getTarget();
                    visited.add(id);
                    queue.addLast(target);
                }
            }
        }
        return visited.size();
    }
}
