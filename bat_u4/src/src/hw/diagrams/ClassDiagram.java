package hw.diagrams;

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.AttributeQueryType;
import com.oocourse.uml3.interact.common.OperationQueryType;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import hw.elements.MyClass;
import hw.elements.MyInterface;
import hw.elements.MyOperation;
import hw.interfaces.AssociatedItem;
import hw.interfaces.Operable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassDiagram {
    private static final int MapCapacity = 128;
    private Map<String, List<MyClass>> name2class = new HashMap<>(MapCapacity);
    private List<MyClass> classList = new ArrayList<>(MapCapacity);
    private List<MyInterface> interfList = new ArrayList<>(MapCapacity);
    // temp variable
    private Map<String, Object> id2elem;
    private Map<String,  String> endid2classid = new HashMap<>(MapCapacity);

    public ClassDiagram(Map<String, Object> id2elem) {
        this.id2elem = id2elem;
    }

    public void parseUmlClass(UmlClass umlCla) {
        MyClass myCla = new MyClass(umlCla);
        id2elem.put(umlCla.getId(), myCla);
        if (name2class.containsKey(umlCla.getName())) {
            name2class.get(umlCla.getName()).add(myCla);
        } else {
            List<MyClass> ls = new ArrayList<>();
            ls.add(myCla);
            name2class.put(umlCla.getName(), ls);
        }
        classList.add(myCla);
    }

    public void parseUmlInterf(UmlInterface umlInterf) {
        MyInterface myInterf = new MyInterface(umlInterf);
        id2elem.put(umlInterf.getId(), myInterf);
        interfList.add(myInterf);
    }

    public void parseUmlAssociationEnd(UmlAssociationEnd umlAssocEnd) {
        // 应该是加入对端，不是加入自己这一端
        /*MyClass myCla = (MyClass) id2elem.get(umlAssocEnd.getReference());
        myCla.addAssociationEnd(umlAssocEnd);*/
        endid2classid.put(umlAssocEnd.getId(), umlAssocEnd.getReference());
        id2elem.put(umlAssocEnd.getId(), umlAssocEnd);
    }

    public void parseUmlAttribute(UmlAttribute umlAttr) {
        // Attribute的父母也可能是UMLCollaboration
        Object obj = id2elem.get(umlAttr.getParentId());
        if (obj instanceof Operable) {
            Operable target = (Operable) obj;
            target.addAttribute(umlAttr);
        }

    }

    public void parseUmlGeneration(UmlGeneralization umlGener) {
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

    public void parseUmlInterfRealization(UmlInterfaceRealization interfaceRealization) {
        Object sourceObj = id2elem.get(interfaceRealization.getSource());
        Object targetObj = id2elem.get(interfaceRealization.getTarget());
        if (sourceObj instanceof MyClass && targetObj instanceof MyInterface) {
            ((MyClass) sourceObj).addImplementedInterf((MyInterface) targetObj);
            return;
        }
        System.out.println(
                "In java, the only permmited situation is that class realize interface");
    }

    public void parseUmlOperation(UmlOperation op) {
        Operable target = (Operable) id2elem.get(op.getParentId());
        MyOperation myOp = new MyOperation(op);
        target.addOperation(myOp);
        id2elem.put(op.getId(), myOp);
    }

    public void parseUmlAssociation(UmlAssociation umlAssoc) {
        AssociatedItem item1 = (AssociatedItem) id2elem.get(endid2classid.get(umlAssoc.getEnd1()));
        AssociatedItem item2 = (AssociatedItem) id2elem.get(endid2classid.get(umlAssoc.getEnd2()));
        // 自关联同样算两次
        item1.addAssociatedItem(item2);
        item2.addAssociatedItem(item1);

        // add the other end to the class
        UmlAssociationEnd end1 = (UmlAssociationEnd) id2elem.get(umlAssoc.getEnd1());
        UmlAssociationEnd end2 = (UmlAssociationEnd) id2elem.get(umlAssoc.getEnd2());
        item1.addAssociationEnd(end2);
        item2.addAssociationEnd(end1);

    }

    public void parseUmlParam(UmlParameter param) {
        MyOperation myOp = (MyOperation) id2elem.get(param.getParentId());
        myOp.addParam(param);
    }

    private MyClass checkClassName(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        List<MyClass> classLs = name2class.get(className);
        if (classLs == null) {
            throw new ClassNotFoundException(className);
        }

        if (classLs.size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return classLs.get(0);

    }

    public int getCLassCount() {
        return classList.size();
    }

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

    public int getClassAttributeCount(String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAttrNum(queryType);
    }

    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAssoCnt();
    }

    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAssociatedClassList();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getClassOperationVisibility(operationName);
    }

    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAttrVisibility(attributeName);
    }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getTopParentClass();
    }

    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getImplementedInterfList();
    }

    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass cla = checkClassName(className);
        return cla.getAttributeNotHidden();
    }

    public void checkForUml001() throws UmlRule001Exception {
        Set<AttributeClassInformation> inforSet = new HashSet<>();
        for (MyClass mycla : classList) {
            List<String> duplicatedMemberList = mycla.checkDuplicatedMember();
            for (String memberName : duplicatedMemberList) {
                inforSet.add(new AttributeClassInformation(memberName, mycla.getClassName()));
            }
        }

        if (!inforSet.isEmpty()) {
            throw new UmlRule001Exception(inforSet);
        }
    }

    /**
     * 对于类的循环继承，不采用对每个点都遍历一遍
     * 在之前其他点的遍历过程中访问过的点，轮到遍历该点时，跳过此点的遍历
     * 因为类是单继承，父节点唯一。 如果有环，访问环上任意一点即可获得整个环
     *为了实现的简单起见，还是遍历每一个结点
     *
     * 对于接口，必须遍历每一个结点
     * 时间复杂度O(V(V+E))
     * @throws UmlRule002Exception
     */
    public void checkForUml002() throws UmlRule002Exception {
        Set<UmlClassOrInterface> claOrInterfSet = new HashSet<>(MapCapacity);
        // class
        for (MyClass cla : classList) {
            if (cla.checkCircularInheritance()) {
                claOrInterfSet.add(cla.getUmlClass());
            }
        }
        // interf
        for (MyInterface interf : interfList) {
            if (interf.checkCircularInheritance()) {
                claOrInterfSet.add(interf.getUmlInterf());
            }
        }
        // check
        if (!claOrInterfSet.isEmpty()) {
            throw new UmlRule002Exception(claOrInterfSet);
        }

    }

    public void checkForUml003() throws UmlRule003Exception {
        // 由于类不允许多继承，目前没有考虑类的重复继承
        Set<UmlClassOrInterface> claOrInterfSet = new HashSet<>();
        for (MyInterface interf : interfList) {
            if (!interf.checkDuplicatedGeneralization()) {
                claOrInterfSet.add(interf.getUmlInterf());
            }
        }
        if (!claOrInterfSet.isEmpty()) {
            throw new UmlRule003Exception(claOrInterfSet);
        }

    }

    public void checkForUml004() throws UmlRule004Exception {
        Set<UmlClassOrInterface> claOrInterfSet = new HashSet<>();
        for (MyClass cla : classList) {
            if (!cla.checkDuplicatedRealization()) {
                claOrInterfSet.add(cla.getUmlClass());
            }
        }
        if (!claOrInterfSet.isEmpty()) {
            throw new UmlRule004Exception(claOrInterfSet);
        }
    }

    public void checkForUml005() throws UmlRule005Exception {
        for (MyClass mycla : classList) {
            mycla.checkElemName();
        }
        for (MyInterface interf : interfList) {
            interf.checkElemName();
        }
    }

    public void checkForUml006() throws UmlRule006Exception {
        for (MyInterface interf : interfList) {
            interf.checkAttrVisibility();
        }
    }

}
