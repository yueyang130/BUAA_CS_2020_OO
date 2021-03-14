package myelements.collaboration;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlLifeline;
import myelements.MyUmlElement;

public class MyUmlLifeline extends MyUmlElement {
    public MyUmlLifeline(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlLifeline) super.getUmlElement()).getVisibility();
    }
    
    public String getRepresent() {
        return ((UmlLifeline) super.getUmlElement()).getRepresent();
    }
    
    public boolean isMultiInstance() {
        return ((UmlLifeline) super.getUmlElement()).isMultiInstance();
    }
}
