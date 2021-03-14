import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;

import java.util.Collection;

public class UmlState extends UmlElement {
    private Visibility visibility;
    private ElementType type;
    // the key of subsequences is the id of target class
    private MultiMap<String, UmlTransition> subsequences;

    public UmlState(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        type = element.getElementType();
        switch (type) {
            case UML_PSEUDOSTATE: {
                UmlPseudostate e = (UmlPseudostate) element;
                visibility = e.getVisibility();
                break;
            }
            case UML_FINAL_STATE: {
                UmlFinalState e = (UmlFinalState) element;
                visibility = e.getVisibility();
                break;
            }
            case UML_STATE: {
                com.oocourse.uml3.models.elements.UmlState e =
                        (com.oocourse.uml3.models.elements.UmlState) element;
                visibility = e.getVisibility();
                break;
            }
            default:
        }
        subsequences = new MultiMap<>();
    }

    public ElementType getType() {
        //return type;
        return ElementType.UML_STATE;
    }

    public int getSubsequenceCount() { return subsequences.realSize(); }

    public Visibility getVisibility() {
        return visibility;
    }

    public void addSubsequence(UmlTransition state) {
        subsequences.put(state.getTargetId(), state);
    }

    public Collection<UmlTransition> getSubsequences() {
        return subsequences.values();
    }
}
