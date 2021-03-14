package myelements.classmodel;

import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import myelements.MyUmlElement;

public class MyUmlInterfaceRealization extends MyUmlElement {
    public MyUmlInterfaceRealization(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public String getSource() {
        return ((UmlInterfaceRealization) super.getUmlElement()).getSource();
    }
    
    public String getTarget() {
        return ((UmlInterfaceRealization) super.getUmlElement()).getTarget();
    }
}
