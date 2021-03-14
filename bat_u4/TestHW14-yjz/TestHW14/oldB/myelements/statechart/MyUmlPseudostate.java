package myelements.statechart;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import myelements.MyUmlElement;

public class MyUmlPseudostate extends MyUmlElement {
    public MyUmlPseudostate(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlPseudostate) super.getUmlElement()).getVisibility();
    }
}
