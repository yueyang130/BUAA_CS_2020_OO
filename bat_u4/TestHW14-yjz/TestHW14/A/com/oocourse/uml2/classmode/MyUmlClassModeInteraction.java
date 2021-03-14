package com.oocourse.uml2.classmode;

import com.oocourse.uml2.function.Checker;
import com.oocourse.uml2.function.Initialer;
import com.oocourse.uml2.general.ElementInformation;
import com.oocourse.uml2.general.Node;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml2.interact.format.UmlClassModelInteraction;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MyUmlClassModeInteraction implements UmlClassModelInteraction {
    private ElementInformation eleInfo;

    public MyUmlClassModeInteraction(ElementInformation eleInfo) {
        this.eleInfo = eleInfo;
    }

    private UmlClass getClassElement(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = null;
        for (UmlClass tempClassElement : eleInfo.getClassElements()) {
            if (tempClassElement.getName().equals(className)) {
                if (classElement == null) {
                    classElement = tempClassElement;
                } else {
                    throw new ClassDuplicatedException(className);
                }
            }
        }
        if (classElement == null) {
            throw new ClassNotFoundException(className);
        }
        return classElement;
    }

    private UmlAttribute getAttributeElement(UmlClass classElement, String attributeName)
        throws AttributeNotFoundException, AttributeDuplicatedException {
        UmlAttribute attributeElement = null;
        String tempId = classElement.getId();
        while (true) {
            Node tempNode = eleInfo.getIdTree().get(tempId);
            HashSet<String> attributeChildIds = tempNode.getAttributeChildIds();
            for (String attributeChildId : attributeChildIds) {
                UmlAttribute tempAttributeElement =
                    (UmlAttribute) eleInfo.getElementMap().get(attributeChildId);
                if (tempAttributeElement.getName().equals(attributeName)) {
                    if (attributeElement == null) {
                        attributeElement = tempAttributeElement;
                    } else {
                        throw new
                            AttributeDuplicatedException(classElement.getName(), attributeName);
                    }
                }
            }
            HashSet<String> extendIds = eleInfo.getExtendPic().get(tempId);
            if (extendIds == null) {
                break;
            }
            tempId = extendIds.iterator().next();
        }

        if (attributeElement == null) {
            throw new AttributeNotFoundException(classElement.getName(), attributeName);
        }
        return attributeElement;
    }

    private String id2Name(String id) {
        UmlElement element = eleInfo.getElementMap().get(id);
        return element.getName();
    }

    @Override
    public int getClassCount() {
        return eleInfo.getClassElements().size();
    }

    @Override
    public int getClassOperationCount(String className, OperationQueryType[] queryTypes)
        throws ClassNotFoundException, ClassDuplicatedException, ConflictQueryTypeException {
        UmlClass classElement = getClassElement(className);
        String classId = classElement.getId();
        HashSet<OperationQueryType> queryTypeSet = new HashSet(Arrays.asList(queryTypes));
        Checker.checkOperationQueryType(queryTypeSet);
        Node classNode = eleInfo.getIdTree().get(classId);
        HashSet<String> operationChildIds = classNode.getOperationChildIds();

        int ret = 0;
        for (String operationChildId : operationChildIds) {
            HashMap<OperationQueryType, Boolean> operationQueryInfo =
                new HashMap<OperationQueryType, Boolean>();
            Initialer.initOperationQueryInfo(operationQueryInfo);
            Node operationNode = eleInfo.getIdTree().get(operationChildId);
            HashSet<String> parameterChildIds = operationNode.getParameterChildIds();
            boolean flagReturn = false;
            boolean flagParam = false;
            if (!parameterChildIds.isEmpty()) {
                for (String parameterChildId : parameterChildIds) {
                    UmlParameter parameterElement =
                        (UmlParameter) eleInfo.getElementMap().get(parameterChildId);
                    if (!flagReturn) {
                        if (parameterElement.getDirection() == Direction.RETURN) {
                            operationQueryInfo.put(OperationQueryType.NON_RETURN, false);
                            operationQueryInfo.put(OperationQueryType.RETURN, true);
                            flagReturn = true;
                        }
                    }
                    if (!flagParam) {
                        if (parameterElement.getDirection() != Direction.RETURN) {
                            operationQueryInfo.put(OperationQueryType.NON_PARAM, false);
                            operationQueryInfo.put(OperationQueryType.PARAM, true);
                            flagParam = true;
                        }
                    }
                    if (flagReturn && flagParam) {
                        break;
                    }
                }
            }
            ++ret;
            for (OperationQueryType queryType : queryTypeSet) {
                if (operationQueryInfo.get(queryType) == false) {
                    --ret;
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = getClassElement(className);
        String classId = classElement.getId();
        Node classNode = eleInfo.getIdTree().get(classId);

        int ret = classNode.getAttributeChildIds().size();
        if (queryType == AttributeQueryType.SELF_ONLY) {
            return ret;
        }
        String tempId = classId;
        while (true) {
            HashSet<String> extendIds = eleInfo.getExtendPic().get(tempId);
            if (extendIds == null) {
                break;
            }
            String extendId = extendIds.iterator().next();
            Node extendNode = eleInfo.getIdTree().get(extendId);
            ret += extendNode.getAttributeChildIds().size();
            tempId = extendId;
        }
        return ret;
    }

    @Override
    public int getClassAssociationCount(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = getClassElement(className);

        int ret = 0;
        String tempId = classElement.getId();
        while (true) {
            ArrayList<String> associateIds = eleInfo.getAssociatePic().get(tempId);
            if (associateIds != null) {
                ret += associateIds.size();
            }
            HashSet<String> extendIds = eleInfo.getExtendPic().get(tempId);
            if (extendIds == null) {
                break;
            }
            tempId = extendIds.iterator().next();
        }
        return ret;
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = getClassElement(className);

        HashSet<String> visitedIds = new HashSet<String>();
        ArrayList<String> associateClassNames = new ArrayList<String>();
        String tempId = classElement.getId();
        while (true) {
            ArrayList<String> associateIds = eleInfo.getAssociatePic().get(tempId);
            if (associateIds != null) {
                for (String associateId : associateIds) {
                    UmlElement associateElement = eleInfo.getElementMap().get(associateId);
                    if (associateElement.getElementType() == ElementType.UML_CLASS &&
                        !visitedIds.contains(associateId)) {
                        associateClassNames.add(associateElement.getName());
                        visitedIds.add(associateId);
                    }
                }
            }
            HashSet<String> extendIds = eleInfo.getExtendPic().get(tempId);
            if (extendIds == null) {
                break;
            }
            tempId = extendIds.iterator().next();
        }
        return associateClassNames;
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
        String className, String operationName)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = getClassElement(className);
        Node classNode = eleInfo.getIdTree().get(classElement.getId());
        HashSet<String> operationChildIds = classNode.getOperationChildIds();
        HashMap<Visibility, Integer> visibilityInfo = new HashMap<Visibility, Integer>();
        Initialer.initVisibilityInfo(visibilityInfo);

        for (String operationChildId : operationChildIds) {
            UmlOperation operationChildElement =
                (UmlOperation) eleInfo.getElementMap().get(operationChildId);
            if (operationChildElement.getName().equals(operationName)) {
                Visibility visibility = operationChildElement.getVisibility();
                int value = visibilityInfo.get(visibility);
                visibilityInfo.put(visibility, value + 1);
            }
        }
        return visibilityInfo;
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
        throws ClassNotFoundException, ClassDuplicatedException,
        AttributeNotFoundException, AttributeDuplicatedException {
        UmlClass classElement = getClassElement(className);
        UmlAttribute attributeElement = getAttributeElement(classElement, attributeName);
        return attributeElement.getVisibility();
    }

    @Override
    public String getTopParentClass(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = getClassElement(className);

        String tempId = classElement.getId();
        while (true) {
            HashSet<String> extendIds = eleInfo.getExtendPic().get(tempId);
            if (extendIds == null) {
                break;
            }
            String extendId = extendIds.iterator().next();
            if (extendId == null) {
                break;
            }
            tempId = extendId;
        }
        return id2Name(tempId);
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = getClassElement(className);

        HashMap<String, String> allImplements = new HashMap<String, String>();
        String tempId = classElement.getId();
        while (true) {
            HashSet<String> implementIds = eleInfo.getImplementPic().get(tempId);
            if (implementIds != null) {
                for (String implementId : implementIds) {
                    if (allImplements.containsKey(implementId)) {
                        continue;
                    }
                    //  bfs
                    Stack<String> interfaceIds = new Stack<String>();
                    interfaceIds.push(implementId);
                    while (true) {
                        String interfaceId = interfaceIds.pop();
                        allImplements.put(interfaceId, id2Name(interfaceId));
                        HashSet<String> extendInterfaceIds =
                            eleInfo.getExtendPic().get(interfaceId);
                        if (extendInterfaceIds != null) {
                            for (String extendInterfaceId : extendInterfaceIds) {
                                if (!allImplements.containsKey(extendInterfaceId)) {
                                    interfaceIds.push(extendInterfaceId);
                                }
                            }
                        }
                        if (interfaceIds.empty()) {
                            break;
                        }
                    }
                }
            }
            HashSet<String> extendIds = eleInfo.getExtendPic().get(tempId);
            if (extendIds == null) {
                break;
            }
            tempId = extendIds.iterator().next();
        }

        Collection<String> allImplementNameCollection = allImplements.values();
        ArrayList<String> allImplementNames = new ArrayList<String>(allImplementNameCollection);
        return allImplementNames;
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass classElement = getClassElement(className);

        ArrayList<AttributeClassInformation> attributeClassInfos =
            new ArrayList<AttributeClassInformation>();
        String tempId = classElement.getId();
        while (true) {
            Node tempNode = eleInfo.getIdTree().get(tempId);
            HashSet<String> attributeChildIds = tempNode.getAttributeChildIds();
            for (String attributeChildId : attributeChildIds) {
                UmlAttribute attributeChildElement =
                    (UmlAttribute) eleInfo.getElementMap().get(attributeChildId);
                if (!attributeChildElement.getVisibility().equals(Visibility.PRIVATE)) {
                    AttributeClassInformation attributeClassInfo =
                        new AttributeClassInformation(
                            attributeChildElement.getName(), id2Name(tempId));
                    attributeClassInfos.add(attributeClassInfo);
                }
            }
            HashSet<String> extendIds = eleInfo.getExtendPic().get(tempId);
            if (extendIds == null) {
                break;
            }
            tempId = extendIds.iterator().next();
        }
        return attributeClassInfos;
    }
}
