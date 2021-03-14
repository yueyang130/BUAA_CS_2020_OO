package myelements.classmodel;

import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlElement;
import myelements.MyUmlElement;

public class MyUmlAttribute extends MyUmlElement {
    public MyUmlAttribute(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    }
    
    public Visibility getVisibility() {
        return ((UmlAttribute) super.getUmlElement()).getVisibility();
    }
    
    public NameableType getType() {
        return ((UmlAttribute) super.getUmlElement()).getType();
    }
}
