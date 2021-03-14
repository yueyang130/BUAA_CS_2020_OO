package hw.elements;

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.AttributeQueryType;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import hw.interfaces.AssociatedItem;
import hw.interfaces.Operable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MyClass implements AssociatedItem, Operable {
    private int hashSetCapacity = 256;

    private MyClass superClass = null;
    private UmlClass umlClass;
    private List<MyOperation> operations = new ArrayList<>();
    private List<UmlAttribute> attributes = new ArrayList<>();
    private List<UmlAssociationEnd> umlAssociationEnds = new ArrayList<>();
    private List<MyClass> associationClass = new ArrayList<>();
    private List<MyInterface> associationInterfs = new ArrayList<>();
    private List<MyInterface> implemtentedInterfs = new ArrayList<>();

    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public String getClassName() { return umlClass.getName(); }

    public void setSuperClass(MyClass superClass) {
        this.superClass = superClass;
    }

    public MyClass getSuperClass() {
        return superClass;
    }

    public String getTopParentClass() {
        MyClass current = this;
        while (current.superClass != null) {
            current = current.superClass;
        }
        return current.getClassName();
    }

    public void addOperation(MyOperation op) {
        operations.add(op);
    }

    @Override
    public void addAssociationEnd(UmlAssociationEnd end) {
        umlAssociationEnds.add(end);
    }

    public void addAttribute(UmlAttribute attr) {
        attributes.add(attr);
    }

    @Override
    public void addAssociatedItem(AssociatedItem end) {
        if (end instanceof MyClass) {
            associationClass.add((MyClass) end);
        } else if (end instanceof MyInterface) {
            associationInterfs.add((MyInterface) end);
        }
    }

    public int getOpNum(Boolean param, Boolean returnBool) {
        if (param == null && returnBool == null) {
            return operations.size();
        }

        int cnt = 0;
        if (param == null) {
            for (MyOperation op : operations) {
                if (op.hasReturn() == returnBool) {
                    cnt++;
                }
            }
        } else if (returnBool == null) {
            for (MyOperation op : operations) {
                if (op.hasParam() == param) {
                    cnt++;
                }
            }
        } else {
            for (MyOperation op : operations) {
                if (op.hasReturn() == returnBool && op.hasParam() == param) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String opName) {
        Map<Visibility, Integer> visibilityIntegerMap = new EnumMap<>(Visibility.class);
        for (MyOperation op : operations) {
            if (op.getName().equals(opName)) {
                visibilityIntegerMap.merge(op.getVisibility(), 1, Integer::sum);
            }
        }
        return visibilityIntegerMap;
    }

    public int getAttrNum(AttributeQueryType type) {
        if (type == AttributeQueryType.SELF_ONLY) {
            return attributes.size();
        }
        // query all
        // 递推注意终止条件
        if (superClass == null) {
            return attributes.size();
        }
        return attributes.size() + superClass.getAttrNum(AttributeQueryType.ALL);
    }

    /*
    public Visibility getAttrVisibility(String attrName)
            throws AttributeDuplicatedException, AttributeNotFoundException {
        if (superClass == null) {
            int attrCnt = 0;
            Visibility visibilitySelf;
            for (UmlAttribute attr : attributes) {
                if (attr.getName().equals(attrName)) {
                    attrCnt++;
                    visibilitySelf = attr.getVisibility();
                }
            }
            if (attrCnt > 1) { throw new AttributeDuplicatedException(attrName); }

        }
    }
    */

    public Visibility getAttrVisibility(String attrName)
            throws AttributeDuplicatedException, AttributeNotFoundException {
        // 避免爆栈和效率低下的问题，避免递归写法
        List<UmlAttribute> attrAll = new ArrayList<>();
        MyClass current = this;
        while (current != null) {
            attrAll.addAll(current.attributes);
            current = current.superClass;
        }
        int attrCnt = 0;
        Visibility visibility = null;
        for (UmlAttribute attr : attrAll) {
            if (attr.getName().equals(attrName)) {
                attrCnt++;
                visibility = attr.getVisibility();
            }
        }
        if (attrCnt == 0) {
            throw new AttributeNotFoundException(getClassName(), attrName);
        }

        if (attrCnt > 1) {
            throw new AttributeDuplicatedException(getClassName(), attrName);
        }

        return visibility;

    }

    public int getAssoCnt() {
        if (superClass == null) {
            return associationClass.size() + associationInterfs.size();
        }
        return associationClass.size() + associationInterfs.size()
                + superClass.getAssoCnt();
    }

    public UmlClass getUmlClass() { return umlClass; }

    public List<String> getAssociatedClassList() {
        // 不重不漏
        Set<MyClass> classSet = new HashSet<>(hashSetCapacity);
        MyClass current = this;
        while (current != null) {
            classSet.addAll(current.associationClass);
            current = current.superClass;
        }

        // 转化为List
        List<String> associatedClassList = new ArrayList<>(hashSetCapacity);
        for (MyClass cla : classSet) {
            associatedClassList.add(cla.getClassName());
        }
        return associatedClassList;
    }

    public void addImplementedInterf(MyInterface interf) {
        implemtentedInterfs.add(interf);
    }

    public List<String> getImplementedInterfList() {
        // 1.直接实现
        // 2.父类实现
        // 3.接口继承

        // 获得自己和父类实现的所有接口
        Set<MyInterface> interfSet = new HashSet<>(hashSetCapacity);
        MyClass current = this;
        while (current != null) {
            interfSet.addAll(current.implemtentedInterfs);
            current = current.superClass;
        }

        // 获得所有接口的父类
        Queue<MyInterface> interfQueue = new LinkedList<>(interfSet);
        Set<MyInterface> superInterfSet = new HashSet<>(hashSetCapacity);

        while (!interfQueue.isEmpty()) {
            MyInterface currentInterf = interfQueue.poll();
            if (!superInterfSet.contains(currentInterf)) {
                superInterfSet.add(currentInterf);
                interfQueue.addAll(currentInterf.getSuperInterf());
            }
        }

        // 转化为List
        List<String> interfNameList = new ArrayList<>(hashSetCapacity);

        for (MyInterface interf : superInterfSet) {
            interfNameList.add(interf.getName());
        }
        return interfNameList;
    }

    public List<AttributeClassInformation> getAttributeNotHidden() {
        // 子类可能和父类同名，因此这个地方不能用set.或者子类的父类中有两个父类同名
        List<AttributeClassInformation> attrClaInforList = new ArrayList<>();
        MyClass current = this;
        while (current != null) {
            for (UmlAttribute attr : current.attributes) {
                if (attr.getVisibility() != Visibility.PRIVATE) {
                    attrClaInforList.add(
                            new AttributeClassInformation(attr.getName(), current.getClassName()));
                }
            }

            current = current.superClass;
        }
        return attrClaInforList;


        //List<AttributeClassInformation> attrClaInforList = new ArrayList<>();
    }

    public List<String> checkDuplicatedMember() {
        List<String> duplicatedMemberList = new ArrayList<>();
        // check Attr
        Set<String> nameSet = new HashSet<>();
        for (UmlAttribute attr : attributes) {
            // return false if already contains the element
            if (attr.getName() != null) {
                if (!nameSet.add(attr.getName())) {
                    duplicatedMemberList.add(attr.getName());
                }
            }
        }
        // check AssociationEnd
        for (UmlAssociationEnd end : umlAssociationEnds) {
            if (end.getName() != null) {
                if (!nameSet.add(end.getName())) {
                    duplicatedMemberList.add(end.getName());
                }
            }
        }
        return duplicatedMemberList;
    }

    public boolean checkCircularInheritance() {
        Set<UmlClass> circularSet = new HashSet<>();
        MyClass current = this;
        while (current != null) {
            // 如果发现非自身开头的环，则直接退出
            if (!circularSet.add(current.umlClass)) {
                return false;
            }
            current = current.getSuperClass();
            if (current == this) {
                return true;
            }
        }
        return false;

    }

    public void checkElemName() throws UmlRule005Exception {
        // class, attr
        // op, param of non-return
        if (getClassName() == null) {
            throw new UmlRule005Exception();
        }
        for (UmlAttribute attr : attributes) {
            if (attr.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        for (MyOperation op : operations) {
            op.checkOpElemName();
        }

    }

    public boolean checkDuplicatedRealization() {
        // 1.直接实现
        // 2.父类实现
        // 3.接口继承

        // 获得自己和父类实现的所有接口
        Set<MyInterface> interfSet = new HashSet<>(hashSetCapacity);
        MyClass current = this;
        while (current != null) {
            //interfSet.addAll(current.implemtentedInterfs);
            for (MyInterface interf : current.implemtentedInterfs) {
                if (!interfSet.add(interf)) {
                    return false;
                }
            }
            current = current.superClass;
        }

        // 获得所有接口的父类
        Queue<MyInterface> interfQueue = new LinkedList<>(interfSet);
        Set<MyInterface> superInterfSet = new HashSet<>(hashSetCapacity);

        while (!interfQueue.isEmpty()) {
            MyInterface currentInterf = interfQueue.poll();
            if (!superInterfSet.add(currentInterf)) {
                return false;
            } else {
                interfQueue.addAll(currentInterf.getSuperInterf());
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MyClass)) {
            return false;
        }
        MyClass myCla = (MyClass) obj;
        return umlClass.equals(myCla.umlClass);
    }

    @Override
    public int hashCode() {
        return umlClass.hashCode();
    }

}
