package myelements.classmodel;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInterface;
import myelements.MyUmlElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyUmlInterface extends MyUmlElement {
    private HashSet<String> attributes = new HashSet<String>();
    private HashSet<String> operations = new HashSet<String>();
    private HashSet<String> fathers = new HashSet<String>();
    private HashSet<String> sons = new HashSet<String>();
    private HashSet<String> associations = new HashSet<String>();
    
    public MyUmlInterface(UmlElement umlElement) {
        super(umlElement);
    }
    
    @Override
    public void link(MyUmlElement myUmlElement) {
        ElementType elementType = myUmlElement.getElementType();
        switch (elementType) {
            case UML_ATTRIBUTE:
                attributes.add(myUmlElement.getId());
                break;
            case UML_OPERATION:
                operations.add(myUmlElement.getId());
                break;
            case UML_INTERFACE_REALIZATION:
                String source = ((MyUmlInterfaceRealization)myUmlElement).getSource();
                String targert = ((MyUmlInterfaceRealization)myUmlElement).getTarget();
                String id = super.getId();
                if (source != null && source.equals(id)) {
                    fathers.add(targert);
                } else if (targert != null && targert.equals(id)) {
                    sons.add(source);
                }
                break;
            case UML_GENERALIZATION:
                String sou = ((MyUmlGeneralization)myUmlElement).getSource();
                String tar = ((MyUmlGeneralization)myUmlElement).getTarget();
                String idd = super.getId();
                if (sou != null && sou.equals(idd)) {
                    fathers.add(tar);
                } else if (tar != null && tar.equals(idd)) {
                    sons.add(sou);
                }
                break;
            case UML_ASSOCIATION:
                associations.add(myUmlElement.getId());
                break;
            default:
        }
    }
    
    public Visibility getVisibility() {
        return ((UmlInterface) super.getUmlElement()).getVisibility();
    }
    
    public HashMap<String,String> getImplementInterfaceList(
            HashMap<String,MyUmlElement> elementsIdMap, HashSet<String> visited) {
        HashMap<String,String> res = new HashMap<String, String>();
        for (Iterator<String> it = fathers.iterator(); it.hasNext();) {
            String interfaceid = it.next();
            MyUmlInterface myUmlInterface = (MyUmlInterface) elementsIdMap.get(interfaceid);
            if (!visited.contains(interfaceid)) {
                visited.add(interfaceid);
                res.put(interfaceid, myUmlInterface.getName());
                res.putAll(myUmlInterface.getImplementInterfaceList(elementsIdMap, visited));
            }
        }
        return res;
    }
}
