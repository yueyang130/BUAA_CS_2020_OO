package myelements.classmodel;

import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlParameter;
import myelements.MyUmlElement;

public class MyUmlParamter extends MyUmlElement {
    public MyUmlParamter(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Direction getDirection() {
        return ((UmlParameter) super.getUmlElement()).getDirection();
    }
    
    public NameableType getType() {
        return ((UmlParameter) super.getUmlElement()).getType();
    }
}
