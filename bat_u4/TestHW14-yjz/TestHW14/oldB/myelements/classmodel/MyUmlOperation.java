package myelements.classmodel;

import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlOperation;
import myelements.MyUmlElement;

import java.util.HashSet;

public class MyUmlOperation extends MyUmlElement {
    private HashSet<String> paramters = new HashSet<String>();
    private int retur = 0;
    private int parm = 0;
    
    public MyUmlOperation(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
        ElementType elementType = myUmlElement.getElementType();
        switch (elementType) {
            case UML_PARAMETER:
                paramters.add(myUmlElement.getId());
                Direction direction = ((MyUmlParamter)myUmlElement).getDirection();
                if (direction.equals(Direction.RETURN)) {
                    retur = 1;
                } else {
                    parm = 1;
                }
                break;
            default:
        }
    }
    
    public HashSet<String> getParamters() {
        return paramters;
    }
    
    public int getRetur() {
        return retur;
    }
    
    public int getParm() {
        return parm;
    }
    
    public Visibility getVisibility() {
        return ((UmlOperation) super.getUmlElement()).getVisibility();
    }
}
