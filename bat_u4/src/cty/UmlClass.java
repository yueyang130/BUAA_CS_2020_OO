import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.AttributeQueryType;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;

public class UmlClass extends UmlExtendable {
    private Visibility visibility;

    private MultiMap<String, UmlClass> parents;
    private MultiMap<String, UmlInterface> interfaces;
    private MultiMap<String, UmlAttribute> properties;
    private MultiMap<String, UmlOperation> methods;
    private MultiMap<String, UmlDirectedAssociation> associations;
    private MultiMap<String, UmlElement> attributes;

    private int propertyCount = 0;
    private LinkedList<AttributeClassInformation> nonPrivates;

    private int methodCount = 0;
    private int methodCountReturnParam = 0;
    private int methodCountReturn = 0;
    private int methodCountParam = 0;

    private int associationCount = 0;

    public UmlClass(com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlClass e =
                (com.oocourse.uml3.models.elements.UmlClass) element;
        this.visibility = e.getVisibility();
        parents = new MultiMap<>();
        interfaces = new MultiMap<>();
        properties = new MultiMap<>();
        methods = new MultiMap<>();
        associations = new MultiMap<>();
        nonPrivates = new LinkedList<>();
        attributes = new MultiMap<>();
    }

    public ElementType getType() { return ElementType.UML_CLASS; }

    public void addAssociation(UmlDirectedAssociation association) {
        associationCount += 1;
        associations.put(association.getName(), association);
        attributes.put(association.getName(), association);
    }

    public HashSet<AttributeClassInformation> getDuplicatedNames() {
        HashSet<AttributeClassInformation> res = new HashSet<>();
        for (Map.Entry<String, LinkedList<UmlElement>> e: attributes.entrySet()) {
            String name = e.getKey();
            if (name != null && e.getValue().size() > 1) {
                res.add(new AttributeClassInformation(name, getName()));
            }
        }
        return res;
    }

    public void addParent(UmlClass parent) {
        parents.put(parent.getName(), parent);
        addUmlParent(parent);
    }

    public void addInterface(UmlInterface uinterface) {
        interfaces.put(uinterface.getName(), uinterface);
        addUmlParent(uinterface);
    }

    public void addProperty(UmlAttribute property) {
        propertyCount += 1;
        properties.put(property.getName(), property);
        attributes.put(property.getName(), property);
        if (property.getVisibility() != Visibility.PRIVATE) {
            nonPrivates.add(new AttributeClassInformation(property.getName(),
                    getName()));
        }
    }

    public void addMethod(UmlOperation method) {
        methodCount += 1;
        methods.put(method.getName(), method);
        if (method.isNonParam()) {
            if (!method.isNonReturn()) {
                methodCountReturn += 1;
            }
        } else {
            methodCountParam += 1;
            if (!method.isNonReturn()) {
                methodCountReturn += 1;
                methodCountReturnParam += 1;
            }
        }
    }

    public int getPropertyCount(AttributeQueryType qtype) {
        int res = propertyCount;
        if (qtype == AttributeQueryType.ALL && hasParent()) {
            return res + getParent().getPropertyCount(qtype);
        }
        return res;
    }

    public int getOperationCount(UInt.QueryEnum qreturn, UInt.QueryEnum qparam)
    {
        switch (qreturn) {
            case NO: {
                switch (qparam) {
                    case NO: return methodCount + methodCountReturnParam -
                            methodCountReturn - methodCountParam;
                    case YES: return methodCountParam - methodCountReturnParam;
                    case ALL: return methodCount - methodCountReturn;
                    default: return 0;
                }
            }
            case YES: {
                switch (qparam) {
                    case NO: return methodCountReturn - methodCountReturnParam;
                    case YES: return methodCountReturnParam;
                    case ALL: return methodCountReturn;
                    default: return 0;
                }
            }
            case ALL: {
                switch (qparam) {
                    case NO: return methodCount - methodCountParam;
                    case YES: return methodCountParam;
                    case ALL: return methodCount;
                    default: return 0;
                }
            }
            default: return 0;
        }
    }

    public int getAssociationCount() {
        if (hasParent()) {
            return associationCount + parents.firstValue().
                    getAssociationCount();
        }
        return associationCount;
    }

    public HashSet<UmlClass> getAssociationList() {
        HashSet<UmlClass> res;
        if (hasParent()) {
            res = getParent().getAssociationList();
        } else {
            res = new HashSet<>();
        }
        for (UmlDirectedAssociation association: associations.values()) {
            if (association.isClassAssociation()) {
                res.add((UmlClass) association.getTarget());
            }
        }
        return res;
    }

    public Map<Visibility, Integer> getOperationVisibility(String methodName) {
        Map<Visibility, Integer> qv = new HashMap<>();
        qv.put(Visibility.PUBLIC, 0);
        qv.put(Visibility.PRIVATE, 0);
        qv.put(Visibility.PROTECTED, 0);
        qv.put(Visibility.PACKAGE, 0);
        Iterator<UmlOperation> it = methods.keyIterator(methodName);
        while (it.hasNext()) {
            UmlOperation op = it.next();
            switch (op.getVisibility()) {
                case PUBLIC: {
                    qv.put(Visibility.PUBLIC, qv.get(Visibility.PUBLIC) + 1);
                    break;
                }
                case PRIVATE: {
                    qv.put(Visibility.PRIVATE, qv.get(Visibility.PRIVATE) + 1);
                    break;
                }
                case PROTECTED: {
                    qv.put(Visibility.PROTECTED,
                            qv.get(Visibility.PROTECTED) + 1);
                    break;
                }
                case PACKAGE: {
                    qv.put(Visibility.PACKAGE, qv.get(Visibility.PACKAGE) + 1);
                    break;
                }
                default:
            }
        }
        return qv;
    }

    public Visibility getAttributeVisibility(String propertyName) throws
            AttributeDuplicatedException, AttributeNotFoundException {
        int sz = properties.keySize(propertyName);
        if (sz > 1) {
            throw new AttributeDuplicatedException(getName(), propertyName);
        }
        if (sz == 0) {
            if (hasParent()) {
                try {
                    return getParent().getAttributeVisibility(propertyName);
                } catch (AttributeNotFoundException e) {
                    throw new AttributeNotFoundException(getName(),
                            propertyName);
                } catch (AttributeDuplicatedException e) {
                    throw new AttributeDuplicatedException(getName(),
                            propertyName);
                }
            } else {
                throw new AttributeNotFoundException(getName(), propertyName);
            }
        }
        Visibility res = properties.get(propertyName).getVisibility();
        if (hasParent()) {
            try {
                getParent().getAttributeVisibility(propertyName);
            } catch (AttributeNotFoundException e) {
                return res;
            } catch (AttributeDuplicatedException e) {
                throw new AttributeDuplicatedException(getName(), propertyName);
            }
            throw new AttributeDuplicatedException(getName(), propertyName);
        }
        return res;
    }

    public String getTopClassName() {
        if (hasParent()) {
            return getParent().getTopClassName();
        }
        return getName();
    }

    public HashSet<UmlInterface> getImplements() {
        HashSet<UmlInterface> res = new HashSet<>();
        HashSet<String> visited = new HashSet<>();
        LinkedList<UmlInterface> stack = new LinkedList<>();
        UmlClass cls = this;
        while (cls != null) {
            for (UmlInterface i : cls.getDirectInterfaces()) {
                stack.add(i);
            }
            cls = cls.getParent();
        }
        while (!stack.isEmpty()) {
            UmlInterface i = stack.removeFirst();
            String id = i.getId();
            if (!visited.contains(id)) {
                visited.add(id);
                res.add(i);
                stack.addAll(i.getDirectParents());
            }
        }
        return res;
    }

    public Collection<UmlInterface> getDirectInterfaces() {
        return interfaces.values();
    }

    public LinkedList<AttributeClassInformation> getInfoHidden() {
        LinkedList<AttributeClassInformation> res;
        if (hasParent()) {
            res = getParent().getInfoHidden();
        } else {
            res = new LinkedList<>();
        }
        res.addAll(nonPrivates);
        return res;
    }

    public UmlClass getParent() {
        return parents.firstValue();
    }

    public boolean hasParent() {
        return !parents.isEmpty();
    }
}
