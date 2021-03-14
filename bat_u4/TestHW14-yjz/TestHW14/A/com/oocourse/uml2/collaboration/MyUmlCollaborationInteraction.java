package com.oocourse.uml2.collaboration;

import com.oocourse.uml2.general.ElementInformation;
import com.oocourse.uml2.general.Node;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private ElementInformation eleInfo;

    public MyUmlCollaborationInteraction(ElementInformation eleInfo) {
        this.eleInfo = eleInfo;
    }

    private UmlInteraction getInteractionElement(String interactionName)
        throws InteractionNotFoundException, InteractionDuplicatedException {
        UmlInteraction interactionElement = null;
        for (UmlInteraction tempInteractionElement : eleInfo.getInteractionElements()) {
            String tempInteractionName = tempInteractionElement.getName();
            if (tempInteractionName != null) {
                if (tempInteractionName.equals(interactionName)) {
                    if (interactionElement == null) {
                        interactionElement = tempInteractionElement;
                    } else {
                        throw new InteractionDuplicatedException(interactionName);
                    }
                }
            }
        }
        if (interactionElement == null) {
            throw new InteractionNotFoundException(interactionName);
        }
        return interactionElement;
    }

    private UmlLifeline getLifelineElement(UmlInteraction interactionElement, String lifelineName)
        throws LifelineNotFoundException, LifelineDuplicatedException {
        Node interactionNode = eleInfo.getIdTree().get(interactionElement.getId());
        HashSet<String> lifelineChildIds = interactionNode.getLifelineChildIds();
        UmlLifeline lifelineElement = null;
        for (String lifelineChildId: lifelineChildIds) {
            UmlLifeline lifelineChildElement =
                (UmlLifeline) eleInfo.getElementMap().get(lifelineChildId);
            String lifelineChildName = lifelineChildElement.getName();
            if (lifelineChildName != null) {
                if (lifelineChildName.equals(lifelineName)) {
                    if (lifelineElement == null) {
                        lifelineElement = lifelineChildElement;
                    } else {
                        throw new LifelineDuplicatedException(interactionElement.getName(),
                            lifelineName);
                    }
                }
            }
        }
        if (lifelineElement == null) {
            throw new LifelineNotFoundException(interactionElement.getName(), lifelineName);
        }
        return lifelineElement;
    }

    @Override
    public int getParticipantCount(String interactionName)
        throws InteractionNotFoundException, InteractionDuplicatedException {
        UmlInteraction interactionElement = getInteractionElement(interactionName);
        Node interactionNode = eleInfo.getIdTree().get(interactionElement.getId());
        return interactionNode.getLifelineChildIds().size();
    }

    @Override
    public int getMessageCount(String interactionName)
        throws InteractionNotFoundException, InteractionDuplicatedException {
        UmlInteraction interactionElement = getInteractionElement(interactionName);
        Node interactionNode = eleInfo.getIdTree().get(interactionElement.getId());
        return interactionNode.getMessageChildIds().size();
    }

    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
        throws InteractionNotFoundException, InteractionDuplicatedException,
        LifelineNotFoundException, LifelineDuplicatedException {
        UmlInteraction interactionElement = getInteractionElement(interactionName);
        UmlLifeline lifelineElement = getLifelineElement(interactionElement, lifelineName);
        HashMap<String, ArrayList<String>> messageReversePic =  eleInfo.getMessageReversePic();
        ArrayList<String> incomingObjects = messageReversePic.get(lifelineElement.getId());
        if (incomingObjects == null) {
            return 0;
        }
        return incomingObjects.size();
    }
}
