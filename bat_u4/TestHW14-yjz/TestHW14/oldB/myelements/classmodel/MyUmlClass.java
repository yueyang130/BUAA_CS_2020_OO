package myelements.classmodel;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import myelements.MyUmlElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MyUmlClass extends MyUmlElement {
    private HashSet<String> attributes = new HashSet<String>();
    private HashSet<String> operations = new HashSet<String>();
    private HashSet<String> fathers = new HashSet<String>();
    private HashSet<String> sons = new HashSet<String>();
    private HashSet<String> interfaces = new HashSet<String>();
    private HashSet<String> associations = new HashSet<String>();
    private int associationCount = 0;
    
    public MyUmlClass(UmlElement umlElement) {
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
            case UML_GENERALIZATION:
                String source = ((MyUmlGeneralization)myUmlElement).getSource();
                String targert = ((MyUmlGeneralization)myUmlElement).getTarget();
                String id = super.getId();
                if (source != null && source.equals(id)) {
                    fathers.add(targert);
                } else if (targert != null && targert.equals(id)) {
                    sons.add(source);
                }
                break;
            case UML_INTERFACE_REALIZATION:
                String tar = ((MyUmlInterfaceRealization)myUmlElement).getTarget();
                if (tar != null) {
                    interfaces.add(tar);
                }
                break;
            case UML_ASSOCIATION:
                associations.add(myUmlElement.getId());
                associationCount++;
                break;
            default:
        }
    }
    
    public Visibility getVisibility() {
        return ((UmlClass) super.getUmlElement()).getVisibility();
    }
    
    public HashSet<String> getOperations() {
        return operations;
    }
    
    public HashSet<String> getAttributes() {
        return attributes;
    }
    
    public int getAttributeCount() {
        return attributes.size();
    }
    
    public int dfsGetAttributeCount(HashMap<String,MyUmlElement> elementsIdMap) {
        int res = attributes.size();
        for (Iterator<String> it = fathers.iterator(); it.hasNext();) {
            MyUmlClass father = (MyUmlClass) elementsIdMap.get(it.next());
            res = res + father.dfsGetAttributeCount(elementsIdMap);
        }
        return res;
    }
    
    public int dfsGetAssociationCount(HashMap<String,MyUmlElement> elementsIdMap) {
        int res = associationCount;
        for (Iterator<String> it = fathers.iterator();it.hasNext();) {
            MyUmlClass father = (MyUmlClass) elementsIdMap.get(it.next());
            res = res + father.dfsGetAssociationCount(elementsIdMap);
        }
        return res;
    }
    
    public HashMap<String,String>
        dfsGetAssociatedClassList(HashMap<String,MyUmlElement> elementsIdMap) {
        HashMap<String,String> res = new HashMap<String, String>();
        for (Iterator<String> it = associations.iterator();it.hasNext();) {
            MyUmlAssociation myUmlAssociation = (MyUmlAssociation) elementsIdMap.get(it.next());
            String end1 = myUmlAssociation.getEnd1();
            MyUmlAssociationEnd myend1 = ((MyUmlAssociationEnd) elementsIdMap.get(end1));
            String end1id = myend1.getReference();
            String end2 = myUmlAssociation.getEnd2();
            MyUmlAssociationEnd myend2 = ((MyUmlAssociationEnd) elementsIdMap.get(end2));
            String end2id = myend2.getReference();
            if (end1id.equals(super.getId())) {
                if (elementsIdMap.get(end2id).getElementType().equals(ElementType.UML_CLASS)) {
                    res.put(end2id, elementsIdMap.get(end2id).getName());
                }
            } else {
                if (elementsIdMap.get(end1id).getElementType().equals(ElementType.UML_CLASS)) {
                    res.put(end1id, elementsIdMap.get(end1id).getName());
                }
            }
        }
        for (Iterator<String> it = fathers.iterator();it.hasNext();) {
            MyUmlClass father = (MyUmlClass) elementsIdMap.get(it.next());
            res.putAll(father.dfsGetAssociatedClassList(elementsIdMap));
        }
        return res;
    }
    
    public Visibility dfsGetAttributeVisibility(
            String className, String attributeName,HashMap<String,MyUmlElement> elementsIdMap)
            throws AttributeDuplicatedException {
        int num = 0;
        Visibility res = null;
        for (Iterator<String> it = attributes.iterator();it.hasNext();) {
            MyUmlAttribute attr = (MyUmlAttribute) elementsIdMap.get(it.next());
            if (attr.getName().equals(attributeName)) {
                num++;
                res = attr.getVisibility();
            }
            if (num > 1) {
                throw new AttributeDuplicatedException(className,attributeName);
            }
        }
        for (Iterator<String> it = fathers.iterator();it.hasNext();) {
            MyUmlClass father = (MyUmlClass) elementsIdMap.get(it.next());
            Visibility tmp =
                    father.dfsGetAttributeVisibility(className,attributeName,elementsIdMap);
            if (tmp != null) {
                res = tmp;
                num++;
                if (num > 1) {
                    throw new AttributeDuplicatedException(className,attributeName);
                }
            }
        }
        return res;
    }
    
    public String getTopParentClass(HashMap<String,MyUmlElement> elementsIdMap) {
        if (fathers.size() != 0) {
            for (Iterator<String> it = fathers.iterator();it.hasNext();) {
                MyUmlClass father = (MyUmlClass) elementsIdMap.get(it.next());
                return father.getTopParentClass(elementsIdMap);
            }
        }
        return super.getName();
    }
    
    public HashMap<String,String>
        getImplementInterfaceList(HashMap<String,MyUmlElement> elementsIdMap,
                                  HashSet<String> visited) {
        HashMap<String,String> res = new HashMap<String, String>();
        for (Iterator<String> it = interfaces.iterator();it.hasNext();) {
            String interfaceid = it.next();
            MyUmlInterface myUmlInterface = (MyUmlInterface) elementsIdMap.get(interfaceid);
            if (!visited.contains(interfaceid)) {
                visited.add(interfaceid);
                res.put(interfaceid, myUmlInterface.getName());
                res.putAll(myUmlInterface.getImplementInterfaceList(elementsIdMap, visited));
            }
        }
        for (Iterator<String> it = fathers.iterator();it.hasNext();) {
            String fatherid = it.next();
            MyUmlClass father = (MyUmlClass) elementsIdMap.get(fatherid);
            if (!visited.contains(fatherid)) {
                res.putAll(father.getImplementInterfaceList(elementsIdMap,visited));
            }
        }
        return res;
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(
            HashMap<String,MyUmlElement> elementsIdMap) {
        List<AttributeClassInformation> res = new ArrayList<AttributeClassInformation>();
        for (Iterator<String> it = attributes.iterator();it.hasNext();) {
            MyUmlAttribute myUmlAttribute = (MyUmlAttribute) elementsIdMap.get(it.next());
            if (!myUmlAttribute.getVisibility().equals(Visibility.PRIVATE)) {
                res.add(new AttributeClassInformation(myUmlAttribute.getName(),super.getName()));
            }
        }
        for (Iterator<String> it = fathers.iterator();it.hasNext();) {
            MyUmlClass father = (MyUmlClass) elementsIdMap.get(it.next());
            res.addAll(father.getInformationNotHidden(elementsIdMap));
        }
        return res;
    }
}
