package myelements.statechart;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlState;
import myelements.MyUmlElement;

public class MyUmlState extends MyUmlElement {
    public MyUmlState(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlState) super.getUmlElement()).getVisibility();
    }
}
