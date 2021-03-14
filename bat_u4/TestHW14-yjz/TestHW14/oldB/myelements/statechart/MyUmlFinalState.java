package myelements.statechart;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import myelements.MyUmlElement;

public class MyUmlFinalState extends MyUmlElement {
    public MyUmlFinalState(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlFinalState) super.getUmlElement()).getVisibility();
    }
}
