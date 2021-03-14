package hw.elements;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;

import java.util.HashSet;
import java.util.Set;

public class MyState {
    private UmlElement state;
    private Set<MyState> linkedStates = new HashSet<>();

    public MyState(UmlElement state) {
        if ((state instanceof UmlState) || (state instanceof UmlPseudostate)
            || (state instanceof UmlFinalState)) {
            this.state = state;
            return;
        }
        System.out.println("input must be the instance of UmlState, " +
                "UmlPseudoState or UmlfinalState");
        System.exit(1);
    }

    public ElementType getStateType() {
        return state.getElementType();
    }

    public String getName() {
        return state.getName();
    }

    public Set<MyState> getLinkedStates() {
        return linkedStates;
    }

    public void addSubsequentState(MyState myState) {
        linkedStates.add(myState);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (!(obj instanceof MyState)) { return false; }
        return state.equals(((MyState) obj).state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }
}
