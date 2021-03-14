package myelements.statechart;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlTransition;
import myelements.MyUmlElement;

import java.util.HashSet;

public class MyUmlTransition extends MyUmlElement {
    private HashSet<String> events = new HashSet<String>();
    private HashSet<String> opaquebehaviors = new HashSet<String>();
    
    public MyUmlTransition(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
        ElementType elementType = myUmlElement.getElementType();
        switch (elementType) {
            case UML_EVENT:
                events.add(myUmlElement.getId());
                break;
            case UML_OPAQUE_BEHAVIOR:
                opaquebehaviors.add(myUmlElement.getId());
                break;
            default:
        }
    }
    
    public String getSource() {
        return ((UmlTransition) super.getUmlElement()).getSource();
    }
    
    public String getTarget() {
        return ((UmlTransition) super.getUmlElement()).getTarget();
    }
    
    public Visibility getVisibility() {
        return ((UmlTransition) super.getUmlElement()).getVisibility();
    }
    
    public String getGuard() {
        return ((UmlTransition) super.getUmlElement()).getGuard();
    }
}
