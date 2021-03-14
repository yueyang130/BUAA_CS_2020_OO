package com.oocourse.uml2.general;

import com.oocourse.uml2.function.Checker;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.HashSet;

public class Node {
    private String id;
    private String parentId;
    private HashSet<String> childIds = new HashSet<String>();
    private HashSet<String> operationChildIds = new HashSet<String>();
    private HashSet<String> parameterChildIds = new HashSet<String>();
    private HashSet<String> attributeChildIds = new HashSet<String>();
    private HashSet<String> stateChildIds = new HashSet<String>();
    private HashSet<String> transitionChildIds = new HashSet<String>();
    private HashSet<String> lifelineChildIds = new HashSet<String>();
    private HashSet<String> messageChildIds = new HashSet<String>();
    private String regionChildId;

    public Node(String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public Node(String id) {
        this.id = id;
        this.parentId = "";
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void addChild(UmlElement element) {
        String id = element.getId();
        childIds.add(id);
        if (Checker.isStateElement(element)) {
            stateChildIds.add(id);
        } else {
            switch (element.getElementType()) {
                case UML_OPERATION:
                    operationChildIds.add(id);
                    break;
                case UML_PARAMETER:
                    parameterChildIds.add(id);
                    break;
                case UML_ATTRIBUTE:
                    attributeChildIds.add(id);
                    break;
                case UML_TRANSITION:
                    transitionChildIds.add(id);
                    break;
                case UML_LIFELINE:
                    lifelineChildIds.add(id);
                    break;
                case UML_MESSAGE:
                    messageChildIds.add(id);
                    break;
                case UML_REGION:
                    regionChildId = id;
                    break;
                default:
                    break;
            }
        }
    }

    public HashSet<String> getChildIds() {
        return childIds;
    }

    public HashSet<String> getOperationChildIds() {
        return operationChildIds;
    }

    public HashSet<String> getParameterChildIds() {
        return parameterChildIds;
    }

    public HashSet<String> getAttributeChildIds() {
        return attributeChildIds;
    }

    public HashSet<String> getStateChildIds() {
        return stateChildIds;
    }

    public HashSet<String> getTransitionChildIds() {
        return transitionChildIds;
    }

    public HashSet<String> getLifelineChildIds() {
        return lifelineChildIds;
    }

    public HashSet<String> getMessageChildIds() {
        return messageChildIds;
    }

    public String getRegionChildId() {
        return regionChildId;
    }
}
