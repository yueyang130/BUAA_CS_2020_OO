package myelements.classmodel;

import com.oocourse.uml2.models.common.Aggregation;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlElement;
import myelements.MyUmlElement;

public class MyUmlAssociationEnd extends MyUmlElement {
    public MyUmlAssociationEnd(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlAssociationEnd) super.getUmlElement()).getVisibility();
    }
    
    public String getMultiplicity() {
        return ((UmlAssociationEnd) super.getUmlElement()).getMultiplicity();
    }
    
    public String getReference() {
        return ((UmlAssociationEnd) super.getUmlElement()).getReference();
    }
    
    public Aggregation getAggregation() {
        return ((UmlAssociationEnd) super.getUmlElement()).getAggregation();
    }
}
