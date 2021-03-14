package myelements.statechart;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlOpaqueBehavior;
import myelements.MyUmlElement;

public class MyUmlOpaqueBehavior extends MyUmlElement {
    public MyUmlOpaqueBehavior(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlOpaqueBehavior) super.getUmlElement()).getVisibility();
    }
}
