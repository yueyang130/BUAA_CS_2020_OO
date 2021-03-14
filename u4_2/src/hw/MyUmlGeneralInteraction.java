package hw;

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
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;
import hw.elements.MyClass;
import hw.elements.MyInteraciton;
import hw.elements.MyInterface;
import hw.elements.MyLifeline;
import hw.elements.MyOperation;
import hw.elements.MyRegion;
import hw.elements.MyState;
import hw.elements.MyStateMachine;
import hw.interfaces.AssociatedItem;
import hw.interfaces.Operable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private static final int MapCapacity = 256;
    // class graph
    private Map<String, List<MyClass>> name2class = new HashMap<>(MapCapacity);
    private int classCount = 0;
    // state graph
    private Map<String, List<MyStateMachine>> nameSmMap = new HashMap<>(MapCapacity);
    // collaboration graph
    private Map<String, List<MyInteraciton>> nameInteractionMap = new HashMap<>(MapCapacity);

    // temp variable
    private Map<String, Object> id2elem = new HashMap<>(MapCapacity);
    private Map<String,  String> endid2classid = new HashMap<>(MapCapacity);

    public MyUmlGeneralInteraction(UmlElement[] elements) {
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
            } else if (e instanceof UmlStateMachine) {
                parseUmlStateMachine((UmlStateMachine) e);
            } else if (e instanceof UmlInteraction) {
                parseUmlInteraction((UmlInteraction) e);
            }
        }

        for (UmlElement e : elements) {
            if (e instanceof UmlAssociation) {
                parseUmlAssociation((UmlAssociation) e);
                cnt++;
            } else if (e instanceof UmlParameter) {
                parseUmlParam((UmlParameter) e);
                cnt++;
            } else if (e instanceof UmlRegion) {
                parseUmlRegion((UmlRegion) e);
            } else if (e instanceof UmlLifeline) {
                parseUmlLifeline((UmlLifeline) e);
            }
        }

        for (UmlElement e : elements) {
            if ((e instanceof UmlState) || (e instanceof UmlFinalState)
                || (e instanceof UmlPseudostate)) {
                parseState(e);

            } else if (e instanceof UmlMessage) {
                parseUmlMessage((UmlMessage) e);
            }
        }

        for (UmlElement e : elements) {
            if (e instanceof UmlTransition) {
                parseUmlTranslation((UmlTransition) e);
            }
        }

        /*
        if (cnt != elements.length) {
            System.out.println("FUCKING! Unknown UmlElemenmt Type!");
            System.exit(1);
        }
        */

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
        endid2classid.put(umlAssocEnd.getId(), umlAssocEnd.getReference());
    }

    private void parseUmlAttribute(UmlAttribute umlAttr) {
        // Attribute的父母也可能是UMLCollaboration
        Object obj = id2elem.get(umlAttr.getParentId());
        if (obj instanceof Operable) {
            Operable target = (Operable) obj;
            target.addAttribute(umlAttr);
        }

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
        Object end1Obj = id2elem.get(endid2classid.get(umlAssoc.getEnd1()));
        Object end2Obj = id2elem.get(endid2classid.get(umlAssoc.getEnd2()));
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

    private void parseUmlStateMachine(UmlStateMachine sm) {
        MyStateMachine mySm = new MyStateMachine(sm);
        id2elem.put(sm.getId(), mySm);
        if (nameSmMap.containsKey(sm.getName())) {
            nameSmMap.get(sm.getName()).add(mySm);
        } else {
            List<MyStateMachine> ls = new ArrayList<>();
            ls.add(mySm);
            nameSmMap.put(sm.getName(), ls);
        }
    }

    private void parseUmlRegion(UmlRegion umlRegion) {
        MyStateMachine sm = (MyStateMachine) id2elem.get(umlRegion.getParentId());
        MyRegion myRegion = new MyRegion(umlRegion);
        id2elem.put(umlRegion.getId(), myRegion);
        sm.setRegion(myRegion);
        myRegion.setParent(sm);
    }

    private void parseState(UmlElement state) {
        MyRegion myRegion = (MyRegion) id2elem.get(state.getParentId());
        MyState myState = new MyState(state);
        id2elem.put(state.getId(), myState);
        myRegion.addState(myState);
    }

    private void parseUmlTranslation(UmlTransition umlTrans) {
        String sourceId = umlTrans.getSource();
        String targetId = umlTrans.getTarget();
        MyState sourceState = (MyState) id2elem.get(sourceId);
        MyState targrtState = (MyState) id2elem.get(targetId);
        MyRegion myRegion   = (MyRegion) id2elem.get(umlTrans.getParentId());
        myRegion.addTranslation(umlTrans, sourceState, targrtState);
    }

    private void parseUmlInteraction(UmlInteraction umlInteraction) {
        MyInteraciton myInteraciton = new MyInteraciton(umlInteraction);
        id2elem.put(umlInteraction.getId(), myInteraciton);
        if (nameInteractionMap.containsKey(umlInteraction.getName())) {
            nameInteractionMap.get(umlInteraction.getName()).add(myInteraciton);
        } else {
            nameInteractionMap.put(umlInteraction.getName(), new ArrayList<MyInteraciton>() {
                {
                    add(myInteraciton);
                }
            });
        }
    }

    private void parseUmlLifeline(UmlLifeline umlLifeline) {
        MyLifeline myLifeline = new MyLifeline(umlLifeline);
        id2elem.put(umlLifeline.getId(), myLifeline);
        MyInteraciton myInteraciton = (MyInteraciton) id2elem.get(umlLifeline.getParentId());
        myInteraciton.addParticipant(myLifeline);

    }

    private void parseUmlMessage(UmlMessage umlMsg) {
        // umlMsg的source和target也可能是UMLEndPoint
        Object obj = id2elem.get(umlMsg.getTarget());
        MyInteraciton myInteraciton = (MyInteraciton) id2elem.get(umlMsg.getParentId());
        myInteraciton.addMessage(umlMsg, obj);

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

    private MyStateMachine checkSmName(String smName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        List<MyStateMachine> smLs = nameSmMap.get(smName);
        if (smLs == null) {
            throw new StateMachineNotFoundException(smName);
        }
        if (smLs.size() > 1) {
            throw new StateMachineDuplicatedException(smName);
        }
        return smLs.get(0);
    }

    private MyInteraciton checkInteractionName(String interactionName)
        throws InteractionNotFoundException, InteractionDuplicatedException {

        List<MyInteraciton> interacitonList = nameInteractionMap.get(interactionName);
        if (interacitonList == null) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (interacitonList.size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return interacitonList.get(0);
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

    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraciton myInteraciton = checkInteractionName(interactionName);
        return myInteraciton.getParticipantNum();
    }

    @Override
    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraciton myInteraciton = checkInteractionName(interactionName);
        return myInteraciton.getMsgNum();

    }

    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyInteraciton myInteraciton = checkInteractionName(interactionName);
        return myInteraciton.getIncomingMsgCnt(lifelineName);
    }

    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyStateMachine mySm = checkSmName(stateMachineName);
        return mySm.getStateNum();
    }

    @Override
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyStateMachine mySm = checkSmName(stateMachineName);
        return mySm.getTranslationNum();
    }

    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
                    StateNotFoundException, StateDuplicatedException {
        MyStateMachine mySm = checkSmName(stateMachineName);
        return mySm.getSubsequentStateNum(stateName);
    }
}
