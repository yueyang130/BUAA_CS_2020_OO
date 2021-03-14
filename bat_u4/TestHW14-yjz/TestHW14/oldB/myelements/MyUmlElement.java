package myelements;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;

public abstract class MyUmlElement {
    private UmlElement umlelement;
    
    public MyUmlElement(UmlElement umlElement) {
        this.umlelement = umlElement;
    }
    
    public ElementType getElementType() {
        return umlelement.getElementType();
    }
    
    public String getId() {
        return umlelement.getId();
    }
    
    public String getName() {
        return umlelement.getName();
    }
    
    public String getParentId() {
        return umlelement.getParentId();
    }
    
    public UmlElement getUmlElement() {
        return umlelement;
    }
    
    public abstract void link(MyUmlElement myUmlElement);
}
