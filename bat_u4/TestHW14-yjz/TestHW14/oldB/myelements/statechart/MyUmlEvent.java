package myelements.statechart;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEvent;
import myelements.MyUmlElement;

public class MyUmlEvent extends MyUmlElement {
    public MyUmlEvent(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public String getValue() {
        return ((UmlEvent) super.getUmlElement()).getValue();
    }
    
    public String getExpression() {
        return ((UmlEvent) super.getUmlElement()).getExpression();
    }
    
    public Visibility getVisibility() {
        return ((UmlEvent) super.getUmlElement()).getVisibility();
    }
}
