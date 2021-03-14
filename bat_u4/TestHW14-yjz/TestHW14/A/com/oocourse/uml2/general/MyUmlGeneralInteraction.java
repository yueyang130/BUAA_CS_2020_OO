package com.oocourse.uml2.general;

import com.oocourse.uml2.classmode.MyUmlClassModeInteraction;
import com.oocourse.uml2.collaboration.MyUmlCollaborationInteraction;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.statechart.MyUmlStateCharInteraction;

import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private ElementInformation eleInfo = new ElementInformation();
    private MyUmlCollaborationInteraction umlCollaborationInteraction;
    private MyUmlStateCharInteraction umlStateCharInteraction;
    private MyUmlClassModeInteraction umlClassModeInteraction;

    public MyUmlGeneralInteraction(UmlElement... elements) {
        for (int i = 0; i < elements.length; ++i) {
            eleInfo.addElement(elements[i]);
        }
        eleInfo.initAssociatePic();
        umlCollaborationInteraction = new MyUmlCollaborationInteraction(eleInfo);
        umlStateCharInteraction = new MyUmlStateCharInteraction(eleInfo);
        umlClassModeInteraction = new MyUmlClassModeInteraction(eleInfo);
    }

    @Override
    public int getClassCount() {
        return umlClassModeInteraction.getClassCount();
    }

    @Override
    public int getClassOperationCount(String className, OperationQueryType[] queryTypes)
        throws ClassNotFoundException, ClassDuplicatedException, ConflictQueryTypeException {
        return umlClassModeInteraction.getClassOperationCount(className, queryTypes);
    }

    @Override
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
        throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModeInteraction.getClassAttributeCount(className, queryType);
    }

    @Override
    public int getClassAssociationCount(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModeInteraction.getClassAssociationCount(className);
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModeInteraction.getClassAssociatedClassList(className);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
        String operationName)
        throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModeInteraction.getClassOperationVisibility(className, operationName);
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
        throws ClassNotFoundException, ClassDuplicatedException,
        AttributeNotFoundException, AttributeDuplicatedException {
        return umlClassModeInteraction.getClassAttributeVisibility(className, attributeName);
    }

    @Override
    public String getTopParentClass(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModeInteraction.getTopParentClass(className);
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModeInteraction.getImplementInterfaceList(className);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
        throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModeInteraction.getInformationNotHidden(className);
    }

    @Override
    public int getParticipantCount(String interactionName)
        throws InteractionNotFoundException, InteractionDuplicatedException {
        return umlCollaborationInteraction.getParticipantCount(interactionName);
    }

    @Override
    public int getMessageCount(String interactionName)
        throws InteractionNotFoundException, InteractionDuplicatedException {
        return umlCollaborationInteraction.getMessageCount(interactionName);
    }

    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
        throws InteractionNotFoundException, InteractionDuplicatedException,
        LifelineNotFoundException, LifelineDuplicatedException {
        return umlCollaborationInteraction.getIncomingMessageCount(interactionName, lifelineName);
    }

    @Override
    public int getStateCount(String stateMachineName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return umlStateCharInteraction.getStateCount(stateMachineName);
    }

    @Override
    public int getTransitionCount(String stateMachineName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return umlStateCharInteraction.getTransitionCount(stateMachineName);
    }

    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException,
        StateNotFoundException, StateDuplicatedException {
        return umlStateCharInteraction.getSubsequentStateCount(stateMachineName, stateName);
    }
}
