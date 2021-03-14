package myelements.collaboration;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import myelements.MyUmlElement;

public class MyUmlEndpoint extends MyUmlElement {
    public MyUmlEndpoint(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlEndpoint) super.getUmlElement()).getVisibility();
    }
}
