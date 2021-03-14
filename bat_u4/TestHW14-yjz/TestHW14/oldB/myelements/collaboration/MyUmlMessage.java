package myelements.collaboration;

import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlMessage;
import myelements.MyUmlElement;

public class MyUmlMessage extends MyUmlElement {
    public MyUmlMessage(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
    
    }
    
    public Visibility getVisibility() {
        return ((UmlMessage) super.getUmlElement()).getVisibility();
    }
    
    public String getSource() {
        return ((UmlMessage) super.getUmlElement()).getSource();
    }
    
    public String getTarget() {
        return ((UmlMessage) super.getUmlElement()).getTarget();
    }

    public MessageSort getMessageSort() {
        return ((UmlMessage) super.getUmlElement()).getMessageSort();
    }
}
