package myelements.classmodel;

import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import myelements.MyUmlElement;

public class MyUmlGeneralization extends MyUmlElement {
    public MyUmlGeneralization(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public String getSource() {
        return ((UmlGeneralization) super.getUmlElement()).getSource();
    }
    
    public String getTarget() {
        return ((UmlGeneralization) super.getUmlElement()).getTarget();
    }
}
