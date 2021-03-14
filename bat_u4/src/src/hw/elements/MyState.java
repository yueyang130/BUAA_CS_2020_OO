package hw.elements;

import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyState {
    private UmlElement state;
    //private Set<MyState> linkedStateSet = new HashSet<>();
    private List<MyState> linkedStateList = new ArrayList<>();

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
        return new HashSet<>(linkedStateList);
    }

    public void addSubsequentState(MyState myState) {
        linkedStateList.add(myState);
    }

    public int getTranslationNum() {
        return linkedStateList.size();
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
