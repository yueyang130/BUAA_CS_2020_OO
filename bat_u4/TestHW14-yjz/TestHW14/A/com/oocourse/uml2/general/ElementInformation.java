package com.oocourse.uml2.general;

import com.oocourse.uml2.function.Adder;
import com.oocourse.uml2.function.Initialer;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ElementInformation {
    private HashMap<String, UmlElement> elementMap = new HashMap<String, UmlElement>();
    private HashMap<String, Node> idTree = new HashMap<String, Node>();
    private HashSet<UmlClass> classElements = new HashSet<UmlClass>();
    private HashSet<UmlAssociation> associationElements = new HashSet<UmlAssociation>();
    private HashSet<UmlStateMachine> stateMachineElements = new HashSet<UmlStateMachine>();
    private HashSet<UmlInteraction> interactionElements = new HashSet<UmlInteraction>();
    private HashMap<String, HashSet<String>> extendPic =
        new HashMap<String, HashSet<String>>();
    private HashMap<String, HashSet<String>> implementPic =
        new HashMap<String, HashSet<String>>();
    private HashMap<String, ArrayList<String>> associatePic =
        new HashMap<String, ArrayList<String>>();
    private HashMap<String, HashSet<String>> transitionPic =
        new HashMap<String, HashSet<String>>();
    private HashMap<String, ArrayList<String>> messageReversePic =
        new HashMap<String, ArrayList<String>>();

    public ElementInformation() {
        ;
    }

    public void addElement(UmlElement element) {
        elementMap.put(element.getId(), element);
        Adder.addElement2IdTree(element, idTree);
        switch (element.getElementType()) {
            case UML_CLASS:
                classElements.add((UmlClass) element);
                break;
            case UML_ASSOCIATION:
                associationElements.add((UmlAssociation) element);
                break;
            case UML_STATE_MACHINE:
                stateMachineElements.add((UmlStateMachine) element);
                break;
            case UML_INTERACTION:
                interactionElements.add((UmlInteraction) element);
                break;
            case UML_GENERALIZATION:
                Adder.addRelation2SetPic(((UmlGeneralization) element).getSource(),
                    ((UmlGeneralization) element).getTarget(), extendPic);
                break;
            case UML_INTERFACE_REALIZATION:
                Adder.addRelation2SetPic(((UmlInterfaceRealization) element).getSource(),
                    ((UmlInterfaceRealization) element).getTarget(), implementPic);
                break;
            case UML_TRANSITION:
                Adder.addRelation2SetPic(((UmlTransition) element).getSource(),
                    ((UmlTransition) element).getTarget(), transitionPic);
                break;
            case UML_MESSAGE:
                Adder.addRelation2ListPic(((UmlMessage) element).getTarget(),
                    ((UmlMessage) element).getSource(), messageReversePic);
                break;
            default:
                break;
        }
    }

    public void initAssociatePic() {
        Initialer.initAssociatePic(elementMap, associationElements, associatePic);
    }

    public HashMap<String, UmlElement> getElementMap() {
        return elementMap;
    }

    public HashSet<UmlClass> getClassElements() {
        return classElements;
    }

    public HashSet<UmlAssociation> getAssociationElements() {
        return associationElements;
    }

    public HashSet<UmlInteraction> getInteractionElements() {
        return interactionElements;
    }

    public HashSet<UmlStateMachine> getStateMachineElements() {
        return stateMachineElements;
    }

    public HashMap<String, Node> getIdTree() {
        return idTree;
    }

    public HashMap<String, HashSet<String>> getExtendPic() {
        return extendPic;
    }

    public HashMap<String, HashSet<String>> getImplementPic() {
        return implementPic;
    }

    public HashMap<String, ArrayList<String>> getAssociatePic() {
        return associatePic;
    }

    public HashMap<String, HashSet<String>> getTransitionPic() {
        return transitionPic;
    }

    public HashMap<String, ArrayList<String>> getMessageReversePic() {
        return messageReversePic;
    }
}
