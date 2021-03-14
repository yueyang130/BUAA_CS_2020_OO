package myelements.classmodel;

import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlElement;
import myelements.MyUmlElement;

public class MyUmlAssociation extends MyUmlElement {
    public MyUmlAssociation(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public String getEnd1() {
        return ((UmlAssociation) super.getUmlElement()).getEnd1();
    }
    
    public String getEnd2() {
        return ((UmlAssociation) super.getUmlElement()).getEnd2();
    }
}
