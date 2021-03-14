import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;

import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;

import myelements.classmodel.MyUmlAssociation;
import myelements.classmodel.MyUmlAssociationEnd;
import myelements.classmodel.MyUmlAttribute;
import myelements.classmodel.MyUmlClass;
import myelements.classmodel.MyUmlGeneralization;
import myelements.classmodel.MyUmlInterface;
import myelements.classmodel.MyUmlInterfaceRealization;
import myelements.classmodel.MyUmlOperation;
import myelements.classmodel.MyUmlParamter;
import myelements.collaboration.MyUmlEndpoint;
import myelements.collaboration.MyUmlInteraction;
import myelements.collaboration.MyUmlLifeline;
import myelements.collaboration.MyUmlMessage;
import myelements.statechart.MyUmlFinalState;
import myelements.statechart.MyUmlOpaqueBehavior;
import myelements.statechart.MyUmlPseudostate;
import myelements.statechart.MyUmlRegion;
import myelements.statechart.MyUmlState;
import myelements.statechart.MyUmlStateMachine;
import myelements.statechart.MyUmlTransition;
import myelements.statechart.MyUmlEvent;
import myelements.MyUmlElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private HashMap<String, MyUmlElement> elementsIdMap;
    private int classcount = 0;
    
    private MyUmlElement factory(UmlElement element) {
        switch (element.getElementType()) {
            case UML_CLASS:
                classcount++;
                return new MyUmlClass(element);
            case UML_ASSOCIATION:
                return new MyUmlAssociation(element);
            case UML_ASSOCIATION_END:
                return new MyUmlAssociationEnd(element);
            case UML_ATTRIBUTE:
                return new MyUmlAttribute(element);
            case UML_OPERATION:
                return new MyUmlOperation(element);
            case UML_PARAMETER:
                return new MyUmlParamter(element);
            case UML_GENERALIZATION:
                return new MyUmlGeneralization(element);
            case UML_INTERFACE_REALIZATION:
                return new MyUmlInterfaceRealization(element);
            case UML_INTERFACE:
                return new MyUmlInterface(element);
            case UML_ENDPOINT:
                return new MyUmlEndpoint(element);
            case UML_INTERACTION:
                return new MyUmlInteraction(element);
            case UML_LIFELINE:
                return new MyUmlLifeline(element);
            case UML_MESSAGE:
                return new MyUmlMessage(element);
            case UML_EVENT:
                return new MyUmlEvent(element);
            case UML_FINAL_STATE:
                return new MyUmlFinalState(element);
            case UML_OPAQUE_BEHAVIOR:
                return new MyUmlOpaqueBehavior(element);
            case UML_PSEUDOSTATE:
                return new MyUmlPseudostate(element);
            case UML_REGION:
                return new MyUmlRegion(element);
            case UML_STATE:
                return new MyUmlState(element);
            case UML_STATE_MACHINE:
                return new MyUmlStateMachine(element);
            case UML_TRANSITION:
                return new MyUmlTransition(element);
            default:
                return null;
        }
    }
    
    public MyUmlGeneralInteraction(UmlElement[] elements) {
        this.elementsIdMap = new HashMap<String,MyUmlElement>();
        for (int i = 0;i < elements.length;i++) {
            UmlElement element = elements[i];
            MyUmlElement myUmlElement = factory(element);
            elementsIdMap.put(element.getId(),myUmlElement);
        }
        link(elements);
    }
    
    private void associationLink(UmlAssociation element,MyUmlElement tolink) {
        String end1 = element.getEnd1();
        if (end1 != null && elementsIdMap.containsKey(end1)) {
            MyUmlAssociationEnd myend1 = ((MyUmlAssociationEnd) elementsIdMap.get(end1));
            String end1id = myend1.getReference();
            if (end1id != null && elementsIdMap.containsKey(end1id)) {
                elementsIdMap.get(end1id).link(tolink);
            }
        }
        String end2 = element.getEnd2();
        if (end2 != null && elementsIdMap.containsKey(end2)) {
            MyUmlAssociationEnd myend2 = ((MyUmlAssociationEnd) elementsIdMap.get(end2));
            String end2id = myend2.getReference();
            if (end2id != null && elementsIdMap.containsKey(end2id)) {
                elementsIdMap.get(end2id).link(tolink);
            }
        }
    }
    
    private void parentLink(UmlElement element,MyUmlElement tolink) {
        String parentid = element.getParentId();
        if (parentid != null && elementsIdMap.containsKey(parentid)) {
            elementsIdMap.get(parentid).link(tolink);
        }
    }
    
    private void link(UmlElement[] elements) {
        for (int i = 0;i < elements.length;i++) {
            UmlElement element = elements[i];
            ElementType elementType = element.getElementType();
            MyUmlElement tolink = elementsIdMap.get(element.getId());
            switch (elementType) {
                case UML_ASSOCIATION:
                    associationLink((UmlAssociation) element,tolink);
                    break;
                case UML_ATTRIBUTE:
                case UML_OPERATION:
                case UML_PARAMETER:
                    parentLink(element,tolink);
                    break;
                case UML_GENERALIZATION:
                    String source = ((UmlGeneralization)element).getSource();
                    String targert = ((UmlGeneralization)element).getTarget();
                    if (source != null && elementsIdMap.containsKey(source)) {
                        elementsIdMap.get(source).link(tolink);
                    }
                    if (targert != null && elementsIdMap.containsKey(targert)) {
                        elementsIdMap.get(targert).link(tolink);
                    }
                    break;
                case UML_INTERFACE_REALIZATION:
                    String sou = ((UmlInterfaceRealization)element).getSource();
                    String tar = ((UmlInterfaceRealization)element).getTarget();
                    if (sou != null && elementsIdMap.containsKey(sou)) {
                        elementsIdMap.get(sou).link(tolink);
                    }
                    if (tar != null && elementsIdMap.containsKey(tar)) {
                        elementsIdMap.get(tar).link(tolink);
                    }
                    break;
                case UML_LIFELINE:
                case UML_MESSAGE:
                case UML_ENDPOINT:
                case UML_REGION:
                case UML_PSEUDOSTATE:
                case UML_STATE:
                case UML_FINAL_STATE:
                case UML_EVENT:
                case UML_OPAQUE_BEHAVIOR:
                case UML_TRANSITION:
                    parentLink(element,tolink);
                    break;
                default:
            }
        }
    }
    
    ///////////////////////////////////////////////////////// ClassModel
    
    @Override
    public int getClassCount() {
        return classcount;
    }
    
    private HashMap<String,Integer> conflictQueryTypeCheck(OperationQueryType[] queryTypes)
            throws ConflictQueryTypeException {
        int retur = -1;
        int parm = -1;
        for (int i = 0;i < queryTypes.length;i++) {
            switch (queryTypes[i]) {
                case RETURN:
                    if (retur == 0) {
                        throw new ConflictQueryTypeException();
                    }
                    retur = 1;
                    break;
                case NON_RETURN:
                    if (retur == 1) {
                        throw new ConflictQueryTypeException();
                    }
                    retur = 0;
                    break;
                case PARAM:
                    if (parm == 0) {
                        throw new ConflictQueryTypeException();
                    }
                    parm = 1;
                    break;
                case NON_PARAM:
                    if (parm == 1) {
                        throw new ConflictQueryTypeException();
                    }
                    parm = 0;
                    break;
                default:
            }
        }
        HashMap<String,Integer> type = new HashMap<String,Integer>();
        type.put("return",retur);
        type.put("parm",parm);
        return type;
    }
    
    private MyUmlClass queryClass(String className)
            throws ClassDuplicatedException, ClassNotFoundException {
        int num = 0;
        MyUmlElement target = null;
        for (Map.Entry<String,MyUmlElement> entry : elementsIdMap.entrySet()) {
            MyUmlElement myUmlElement = entry.getValue();
            if (myUmlElement.getName() != null && myUmlElement.getName().equals(className)
                    && myUmlElement.getElementType().equals(ElementType.UML_CLASS)) {
                num++;
                target = myUmlElement;
            }
            if (num > 1) {
                throw new ClassDuplicatedException(className);
            }
        }
        
        if (num == 0) {
            throw new ClassNotFoundException(className);
        }
        return (MyUmlClass)target;
    }
    
    @Override
    public int getClassOperationCount(String className, OperationQueryType[] queryTypes)
            throws ClassNotFoundException, ClassDuplicatedException, ConflictQueryTypeException {
        MyUmlClass target = queryClass(className);
        HashMap<String,Integer> type = conflictQueryTypeCheck(queryTypes);
        int count = 0;
        int retur = type.get("return");
        int parm = type.get("parm");
        HashSet<String> operations = target.getOperations();
        for (Iterator<String> it = operations.iterator(); it.hasNext();) {
            MyUmlOperation op = (MyUmlOperation) elementsIdMap.get(it.next());
            if ((retur == -1 || retur == op.getRetur())
                    && (parm == -1 || parm == op.getParm())) {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass target = queryClass(className);
        if (queryType.equals(AttributeQueryType.SELF_ONLY)) {
            return target.getAttributeCount();
        } else {
            return target.dfsGetAttributeCount(elementsIdMap);
        }
    }
    
    @Override
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass target = queryClass(className);
        return target.dfsGetAssociationCount(elementsIdMap);
    }
    
    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass target = queryClass(className);
        HashMap<String,String> map = target.dfsGetAssociatedClassList(elementsIdMap);
        ArrayList<String> res = new ArrayList<String>();
        for (Map.Entry<String,String> entry : map.entrySet()) {
            res.add(entry.getValue());
        }
        return res;
    }
    
    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass target = queryClass(className);
        Map<Visibility, Integer> res = new HashMap<Visibility, Integer>();
        res.put(Visibility.PUBLIC,0);
        res.put(Visibility.PROTECTED,0);
        res.put(Visibility.PRIVATE,0);
        res.put(Visibility.PACKAGE, 0);
        HashSet<String> operations = target.getOperations();
        for (Iterator<String> it = operations.iterator();it.hasNext();) {
            MyUmlOperation op = (MyUmlOperation) elementsIdMap.get(it.next());
            if (op.getName().equals(operationName)) {
                res.put(op.getVisibility(),res.get(op.getVisibility()) + 1);
            }
        }
        return res;
    }
    
    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyUmlClass target = queryClass(className);
        Visibility res = target.dfsGetAttributeVisibility(className,attributeName,elementsIdMap);
        if (res == null) {
            throw new AttributeNotFoundException(className,attributeName);
        }
        return res;
    }
    
    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass target = queryClass(className);
        return target.getTopParentClass(elementsIdMap);
    }
    
    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass target = queryClass(className);
        HashMap<String,String> map =
                target.getImplementInterfaceList(elementsIdMap, new HashSet<String>());
        ArrayList<String> res = new ArrayList<String>();
        for (Map.Entry<String,String> entry : map.entrySet()) {
            res.add(entry.getValue());
        }
        return res;
    }
    
    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass target = queryClass(className);
        return target.getInformationNotHidden(elementsIdMap);
    }
    
    ///////////////////////////////////////////////////////// Collaboration
    
    private MyUmlInteraction queryInteraction(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        int num = 0;
        MyUmlElement target = null;
        for (Map.Entry<String,MyUmlElement> entry : elementsIdMap.entrySet()) {
            MyUmlElement myUmlElement = entry.getValue();
            if (myUmlElement.getName() != null && myUmlElement.getName().equals(interactionName)
                    && myUmlElement.getElementType().equals(ElementType.UML_INTERACTION)) {
                num++;
                target = myUmlElement;
            }
            if (num > 1) {
                throw new InteractionDuplicatedException(interactionName);
            }
        }
        
        if (num == 0) {
            throw new InteractionNotFoundException(interactionName);
        }
        return (MyUmlInteraction)target;
    }
    
    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyUmlInteraction target = queryInteraction(interactionName);
        return target.getParticipantCount();
    }
    
    @Override
    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyUmlInteraction target = queryInteraction(interactionName);
        return target.getMessageCount();
    }
    
    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyUmlInteraction target = queryInteraction(interactionName);
        return target.getIncomingMessageCount(interactionName, lifelineName, elementsIdMap);
    }
    
    ///////////////////////////////////////////////////////// Collaboration
    
    private MyUmlStateMachine queryStateMachine(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        int num = 0;
        MyUmlElement target = null;
        for (Map.Entry<String,MyUmlElement> entry : elementsIdMap.entrySet()) {
            MyUmlElement myUmlElement = entry.getValue();
            if (myUmlElement.getName() != null && myUmlElement.getName().equals(stateMachineName)
                    && myUmlElement.getElementType().equals(ElementType.UML_STATE_MACHINE)) {
                num++;
                target = myUmlElement;
            }
            if (num > 1) {
                throw new StateMachineDuplicatedException(stateMachineName);
            }
        }
        
        if (num == 0) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        return (MyUmlStateMachine)target;
    }
    
    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyUmlStateMachine myUmlStateMachine = queryStateMachine(stateMachineName);
        return myUmlStateMachine.getStateCount();
    }
    
    @Override
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyUmlStateMachine myUmlStateMachine = queryStateMachine(stateMachineName);
        return myUmlStateMachine.getTransitionCount();
    }
    
    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyUmlStateMachine myUmlStateMachine = queryStateMachine(stateMachineName);
        return myUmlStateMachine.getSubsequentStateCount(stateMachineName, stateName,elementsIdMap);
    }
}
