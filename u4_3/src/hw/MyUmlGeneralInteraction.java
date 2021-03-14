package hw;

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.AttributeQueryType;
import com.oocourse.uml3.interact.common.OperationQueryType;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.PreCheckRuleException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.format.UmlGeneralInteraction;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;
import hw.diagrams.ClassDiagram;
import hw.diagrams.SequenceDiagram;
import hw.diagrams.StateDiagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private static final int MapCapacity = 256;
    // class graph
    private ClassDiagram classDiagram;
    // state graph
    private StateDiagram stateDiagram;
    // sequence graph
    private SequenceDiagram sequenceDiagram;
    // temp variable
    private Map<String, Object> id2elem = new HashMap<>(MapCapacity);

    public MyUmlGeneralInteraction(UmlElement[] elements) {
        classDiagram = new ClassDiagram(id2elem);
        stateDiagram = new StateDiagram(id2elem);
        sequenceDiagram = new SequenceDiagram(id2elem);
        // 1. CLass, Interface
        // 2. AssociationEnd, Attribute, Generalization
        //    InterfRealization, op
        // 3. association, params
        int cnt = 0;
        for (UmlElement e: elements) {
            if (e instanceof UmlClass) {
                classDiagram.parseUmlClass((UmlClass) e);
                cnt++;
            } else if (e instanceof UmlInterface) {
                classDiagram.parseUmlInterf((UmlInterface) e);
                cnt++;
            }
        }

        for (UmlElement e: elements) {
            if (e instanceof UmlAssociationEnd) {
                classDiagram.parseUmlAssociationEnd((UmlAssociationEnd) e);
                cnt++;
            } else if (e instanceof UmlAttribute) {
                classDiagram.parseUmlAttribute((UmlAttribute) e);
                cnt++;
            } else if (e instanceof UmlGeneralization) {
                classDiagram.parseUmlGeneration((UmlGeneralization) e);
                cnt++;
            } else if (e instanceof UmlInterfaceRealization) {
                classDiagram.parseUmlInterfRealization((UmlInterfaceRealization) e);
                cnt++;
            } else if (e instanceof UmlOperation) {
                classDiagram.parseUmlOperation((UmlOperation) e);
                cnt++;
            } else if (e instanceof UmlStateMachine) {
                stateDiagram.parseUmlStateMachine((UmlStateMachine) e);
            } else if (e instanceof UmlInteraction) {
                sequenceDiagram.parseUmlInteraction((UmlInteraction) e);
            }
        }

        for (UmlElement e : elements) {
            if (e instanceof UmlAssociation) {
                classDiagram.parseUmlAssociation((UmlAssociation) e);
                cnt++;
            } else if (e instanceof UmlParameter) {
                classDiagram.parseUmlParam((UmlParameter) e);
                cnt++;
            } else if (e instanceof UmlRegion) {
                stateDiagram.parseUmlRegion((UmlRegion) e);
            } else if (e instanceof UmlLifeline) {
                sequenceDiagram.parseUmlLifeline((UmlLifeline) e);
            }
        }

        for (UmlElement e : elements) {
            if ((e instanceof UmlState) || (e instanceof UmlFinalState)
                || (e instanceof UmlPseudostate)) {
                stateDiagram.parseState(e);

            } else if (e instanceof UmlMessage) {
                sequenceDiagram.parseUmlMessage((UmlMessage) e);
            }
        }

        for (UmlElement e : elements) {
            if (e instanceof UmlTransition) {
                stateDiagram.parseUmlTranslation((UmlTransition) e);
            }
        }

        /*
        if (cnt != elements.length) {
            System.out.println("FUCKING! Unknown UmlElemenmt Type!");
            System.exit(1);
        }
        */

    }

    @Override
    public int getClassCount() {
        return classDiagram.getCLassCount();
    }

    @Override
    public int getClassOperationCount(String className, OperationQueryType[] queryTypes)
            throws ClassNotFoundException, ClassDuplicatedException, ConflictQueryTypeException {
        return classDiagram.getClassOperationCount(className, queryTypes);
    }

    @Override
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAttributeCount(className, queryType);
    }

    @Override
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAssociationCount(className);
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAssociatedClassList(className);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationVisibility(className, operationName);
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return classDiagram.getClassAttributeVisibility(className, attributeName);
    }

    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getTopParentClass(className);
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getImplementInterfaceList(className);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getInformationNotHidden(className);
    }

    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return sequenceDiagram.getParticipantCount(interactionName);
    }

    @Override
    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return sequenceDiagram.getMessageCount(interactionName);
    }

    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return sequenceDiagram.getIncomingMessageCount(interactionName, lifelineName);
    }

    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateDiagram.getStateCount(stateMachineName);
    }

    @Override
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateDiagram.getTransitionCount(stateMachineName);
    }

    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
                    StateNotFoundException, StateDuplicatedException {
        return stateDiagram.getSubsequentStateCount(stateMachineName, stateName);
    }

    @Override
    public void checkForAllRules() throws PreCheckRuleException {
        checkForUml001();
        checkForUml002();
        checkForUml003();
        checkForUml004();
        checkForUml005();
        checkForUml006();
        checkForUml007();
        checkForUml008();
    }

    @Override
    public void checkForUml001() throws UmlRule001Exception {
        classDiagram.checkForUml001();
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        classDiagram.checkForUml002();
    }

    @Override
    public void checkForUml003() throws UmlRule003Exception {
        classDiagram.checkForUml003();
    }

    @Override
    public void checkForUml004() throws UmlRule004Exception {
        classDiagram.checkForUml004();
    }

    @Override
    public void checkForUml005() throws UmlRule005Exception {
        classDiagram.checkForUml005();
    }

    @Override
    public void checkForUml006() throws UmlRule006Exception {
        classDiagram.checkForUml006();
    }

    @Override
    public void checkForUml007() throws UmlRule007Exception {
        stateDiagram.checkForUml007();
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        stateDiagram.checkForUml008();
    }
}
