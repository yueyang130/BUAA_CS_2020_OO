package hw;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlInteraction {
    private static final int MapCapacity = 512;
    private Map<String, Object> id2elem = new HashMap<>(MapCapacity);
    private Map<String, List<MyClass>> name2class = new HashMap<>(MapCapacity);
    private int classCount = 0;
    // temp variable
    private Map<String,  String> end2id = new HashMap<>(MapCapacity);

    public MyUmlInteraction(UmlElement[] elements) {
        // 1. CLass, Interface
        // 2. AssociationEnd, Attribute, Generalization
        //    InterfRealization, op
        // 3. association, params
        int cnt = 0;
        for (UmlElement e: elements) {
            if (e instanceof UmlClass) {
                parseUmlClass((UmlClass) e);
                cnt++;
            } else if (e instanceof UmlInterface) {
                parseUmlInterf((UmlInterface) e);
                cnt++;
            }
        }

        for (UmlElement e: elements) {
            if (e instanceof UmlAssociationEnd) {
                parseUmlAssociationEnd((UmlAssociationEnd) e);
                cnt++;
            } else if (e instanceof UmlAttribute) {
                parseUmlAttribute((UmlAttribute) e);
                cnt++;
            } else if (e instanceof UmlGeneralization) {
                parseUmlGeneration((UmlGeneralization) e);
                cnt++;
            } else if (e instanceof UmlInterfaceRealization) {
                parseUmlInterfRealization((UmlInterfaceRealization) e);
                cnt++;
            } else if (e instanceof UmlOperation) {
                parseUmlOperation((UmlOperation) e);
                cnt++;
            }
        }

        for (UmlElement e : elements) {
            if (e instanceof UmlAssociation) {
                parseUmlAssociation((UmlAssociation) e);
                cnt++;
            } else if (e instanceof UmlParameter) {
                parseUmlParam((UmlParameter) e);
                cnt++;
            }
        }

        if (cnt != elements.length) {
            System.out.println("FUCKING! Unknown UmlElemenmt Type!");
            System.exit(1);
        }
    }

    private void parseUmlClass(UmlClass umlCla) {
        classCount++;
        MyClass myCla = new MyClass(umlCla);
        id2elem.put(umlCla.getId(), myCla);
        if (name2class.containsKey(umlCla.getName())) {
            name2class.get(umlCla.getName()).add(myCla);
        } else {
            List<MyClass> ls = new ArrayList<>();
            ls.add(myCla);
            name2class.put(umlCla.getName(), ls);
        }
    }

    private void parseUmlInterf(UmlInterface umlInterf) {
        MyInterface myInterf = new MyInterface(umlInterf);
        id2elem.put(umlInterf.getId(), myInterf);
    }

    private void parseUmlAssociationEnd(UmlAssociationEnd umlAssocEnd) {
        end2id.put(umlAssocEnd.getId(), umlAssocEnd.getReference());
    }

    private void parseUmlAttribute(UmlAttribute umlAttr) {

        Operable target = (Operable) id2elem.get(umlAttr.getParentId());
        target.addAttribute(umlAttr);
    }

    private void parseUmlGeneration(UmlGeneralization umlGener) {
        Object sourceObj = id2elem.get(umlGener.getSource());
        Object targetObj = id2elem.get(umlGener.getTarget());
        if (sourceObj instanceof MyClass && targetObj instanceof  MyClass) {
            ((MyClass) sourceObj).setSuperClass((MyClass) targetObj);
            return;
        }
        if (sourceObj instanceof MyInterface && targetObj instanceof MyInterface) {
            ((MyInterface) sourceObj).setSuperInterf((MyInterface) targetObj);
            return;
        }
        System.out.println(
                "In java, only class2class and interf2interf generalizaiton is permitted");
        System.exit(1);
    }

    private void parseUmlInterfRealization(UmlInterfaceRealization interfaceRealization) {
        Object sourceObj = id2elem.get(interfaceRealization.getSource());
        Object targetObj = id2elem.get(interfaceRealization.getTarget());
        if (sourceObj instanceof MyClass && targetObj instanceof MyInterface) {
            ((MyClass) sourceObj).addImplementedInterf((MyInterface) targetObj);
            return;
        }
        System.out.println(
                "In java, the only permmited situation is that class realize interface");
    }

    private void parseUmlOperation(UmlOperation op) {
        Operable target = (Operable) id2elem.get(op.getParentId());
        MyOperation myOp = new MyOperation(op);
        target.addOperation(myOp);
        id2elem.put(op.getId(), myOp);
    }

    private void parseUmlAssociation(UmlAssociation umlAssoc) {
        Object end1Obj = id2elem.get(end2id.get(umlAssoc.getEnd1()));
        Object end2Obj = id2elem.get(end2id.get(umlAssoc.getEnd2()));
        AssociatedItem end1 = (AssociatedItem) end1Obj;
        AssociatedItem end2 = (AssociatedItem) end2Obj;
        /*
        // 自关联
        if (end1.equals(end2)) {
            end1.addAssociatedEnd(end2);
        } else {
            end1.addAssociatedEnd(end2);
            end2.addAssociatedEnd(end1);
        }
        */
        // 自关联同样算两次
        end1.addAssociatedEnd(end2);
        end2.addAssociatedEnd(end1);
    }

    private void parseUmlParam(UmlParameter param) {
        MyOperation myOp = (MyOperation) id2elem.get(param.getParentId());
        myOp.addParam(param);
    }

    private MyClass checkClassName(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        List<MyClass> classLs = name2class.get(className);
        if (!name2class.containsKey(className) || classLs.size() == 0) {
            throw new ClassNotFoundException(className);
        }

        if (classLs.size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return classLs.get(0);

    }

    @Override
    public int getClassCount() {
        return classCount;
    }

    @Override
    public int getClassOperationCount(String className, OperationQueryType[] queryTypes)
            throws ClassNotFoundException, ClassDuplicatedException, ConflictQueryTypeException {
        final MyClass mycla = checkClassName(className);

        boolean nonReturn = false;
        boolean hasReturn = false;
        boolean nonParam = false;
        boolean param = false;

        for (OperationQueryType type : queryTypes) {
            if (type == OperationQueryType.NON_RETURN) { nonReturn = true; }
            else if (type == OperationQueryType.RETURN) { hasReturn = true; }
            else if (type == OperationQueryType.NON_PARAM) { nonParam = true; }
            else if (type == OperationQueryType.PARAM) { param = true; }
        }
        if (nonParam && param || nonReturn && hasReturn) {
            throw new ConflictQueryTypeException();
        }

        Boolean returnBool;
        Boolean paramBool;
        // null represents no requires
        if (!hasReturn && !nonReturn) {
            returnBool = null;
        } else {
            returnBool = hasReturn;
        }

        if (!param && !nonParam) {
            paramBool = null;
        } else {
            paramBool = param;
        }

        return mycla.getOpNum(paramBool, returnBool);

    }

    @Override
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAttrNum(queryType);
    }

    @Override
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAssoCnt();
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAssociatedClassList();
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getClassOperationVisibility(operationName);
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAttrVisibility(attributeName);
    }

    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getTopParentClass();
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getImplementedInterfList();
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAttributeNotHidden();
    }
}
