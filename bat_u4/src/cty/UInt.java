import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.AttributeQueryType;
import com.oocourse.uml3.interact.common.OperationQueryType;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.format.UmlGeneralInteraction;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class UInt implements UmlGeneralInteraction {
    public enum QueryEnum { ALL, YES, NO }

    private com.oocourse.uml3.models.elements.UmlElement[]
            rawElements;
    private HashMap<String, UmlElement> elements;
    private MultiMap<String, UmlClass> classes;
    private MultiMap<String, UmlInterface> interfaces;
    private LinkedList<UmlAssociation> associationTemp;
    private LinkedList<UmlOperation> operationTemp;
    private MultiMap<String, UmlInteraction> interactions;
    private MultiMap<String, UmlStateMachine> machines;
    private boolean hasEmptyName = false;
    private LinkedList<UmlState> initialStates;
    private LinkedList<UmlState> finalStates;
    private UIntChecker checker;

    public UInt(com.oocourse.uml3.models.elements.UmlElement... elements) {
        this.rawElements = elements;
        initialStates = new LinkedList<>();
        finalStates = new LinkedList<>();
        classes = new MultiMap<>();
        this.elements = new HashMap<>();
        interactions = new MultiMap<>();
        machines = new MultiMap<>();
        associationTemp = new LinkedList<>();
        operationTemp = new LinkedList<>();
        interfaces = new MultiMap<>();
        elementInitialize();
        elementFill();
        UmlCheck(); }

    private void UmlCheck() {
        checker = new UIntChecker(classes, interfaces); }

    private void elementInitialize() {
        for (int i = 0; i < rawElements.length; ++i) {
            com.oocourse.uml3.models.elements.UmlElement
                    element = rawElements[i];
            checkEmptyName(element);
            switch (element.getElementType()) {
                case UML_CLASS: {
                    UmlClass cls = new UmlClass(element);
                    elements.put(element.getId(), cls);
                    classes.put(element.getName(), cls);
                    break; }
                case UML_INTERFACE: {
                    UmlInterface in = new UmlInterface(element);
                    interfaces.put(element.getName(), in);
                    elements.put(element.getId(), in);
                    break; }
                case UML_ATTRIBUTE: {
                    elements.put(element.getId(), new UmlAttribute(element));
                    break; }
                case UML_OPERATION: {
                    UmlOperation operation = new UmlOperation(element);
                    elements.put(element.getId(), operation);
                    operationTemp.add(operation);
                    break; }
                case UML_PARAMETER: {
                    elements.put(element.getId(), new UmlParameter(element));
                    break; }
                case UML_GENERALIZATION: {
                    elements.put(element.getId(),
                            new UmlGeneralization(element));
                    break; }
                case UML_INTERFACE_REALIZATION: {
                    elements.put(element.getId(),
                            new UmlInterfaceRealization(element));
                    break; }
                case UML_ASSOCIATION: {
                    UmlAssociation association = new UmlAssociation(element);
                    elements.put(element.getId(), association);
                    associationTemp.add(association);
                    break; }
                case UML_ASSOCIATION_END: {
                    elements.put(element.getId(),
                            new UmlAssociationEnd(element));
                    break; }
                default: otherInitialize(element); } } }

    private void otherInitialize(
            com.oocourse.uml3.models.elements.UmlElement element) {
        switch (element.getElementType()) {
            case UML_INTERACTION: {
                UmlInteraction e = new UmlInteraction(element);
                elements.put(element.getId(), e);
                interactions.put(element.getName(), e);
                break; }
            case UML_LIFELINE: {
                elements.put(element.getId(), new UmlLifeline(element));
                break; }
            case UML_ENDPOINT: {
                elements.put(element.getId(), new UmlEndpoint(element));
                break; }
            case UML_MESSAGE: {
                elements.put(element.getId(), new UmlMessage(element));
                break; }
            case UML_STATE_MACHINE: {
                UmlStateMachine e = new UmlStateMachine(element);
                elements.put(element.getId(), e);
                machines.put(element.getName(), e);
                break; }
            case UML_REGION: {
                elements.put(element.getId(), new UmlRegion(element));
                break; }
            case UML_TRANSITION: {
                elements.put(element.getId(), new UmlTransition(element));
                break; }
            case UML_STATE: {
                elements.put(element.getId(), new UmlState(element));
                break; }
            case UML_PSEUDOSTATE: {
                UmlState e = new UmlState(element);
                initialStates.add(e);
                elements.put(element.getId(), e);
                break; }
            case UML_FINAL_STATE: {
                UmlState e = new UmlState(element);
                finalStates.add(e);
                elements.put(element.getId(), e);
                break; }
            default: } }

    private void checkEmptyName(
            com.oocourse.uml3.models.elements.UmlElement e) {
        switch (e.getElementType()) {
            case UML_INTERACTION:
            case UML_LIFELINE:
            case UML_ENDPOINT:
            case UML_MESSAGE:
            case UML_STATE_MACHINE:
            case UML_REGION:
            case UML_TRANSITION:
            case UML_STATE:
            case UML_OPAQUE_BEHAVIOR:
            case UML_EVENT:
            case UML_PSEUDOSTATE:
            case UML_FINAL_STATE:
            case UML_GENERALIZATION:
            case UML_ASSOCIATION:
            case UML_INTERFACE_REALIZATION:
            case UML_ASSOCIATION_END: {
                return; }
            case UML_PARAMETER: {
                if (((com.oocourse.uml3.models.elements.UmlParameter)
                        e).getDirection() == Direction.RETURN) {
                    return; }
                break; }
            case UML_ATTRIBUTE: {
                UmlElement elem = elements.get(e.getParentId());
                if (elem == null) { return; }
                break; }
            default: }
        if (e.getName() == null) { hasEmptyName = true; } }

    private void elementFill() {
        for (Map.Entry<String, UmlElement> ee: elements.entrySet()) {
            UmlElement elem = ee.getValue();
            switch (elem.getType()) {
                case UML_ATTRIBUTE: {
                    UmlAttribute e = (UmlAttribute) elem;
                    UmlElement parent = elements.get(e.getUmlParent());
                    if (parent == null) {
                        break; }
                    switch (parent.getType()) {
                        case UML_CLASS: {
                            UmlClass cls = (UmlClass) parent;
                            cls.addProperty(e);
                            break; }
                        case UML_INTERFACE: {
                            UmlInterface cls = (UmlInterface) parent;
                            cls.addProperty(e);
                            break; }
                        default: }
                    break; }
                case UML_PARAMETER: {
                    UmlParameter e = (UmlParameter) elem;
                    UmlOperation op = (UmlOperation) elements.get(
                            e.getUmlParent());
                    op.addParameter(e);
                    break; }
                case UML_ASSOCIATION: {
                    UmlAssociation e = (UmlAssociation) elem;
                    UmlAssociationEnd end1 = (UmlAssociationEnd)
                            elements.get(e.getEndId1());
                    UmlAssociationEnd end2 = (UmlAssociationEnd)
                            elements.get(e.getEndId2());
                    e.setEnd1(end1);
                    e.setEnd2(end2);
                    break; }
                case UML_ASSOCIATION_END: {
                    UmlAssociationEnd e = (UmlAssociationEnd) elem;
                    UmlElement cls = elements.get(e.getReferenceId());
                    e.setReference((UmlExtendable) cls);
                    break; }
                case UML_GENERALIZATION: {
                    UmlGeneralization e = (UmlGeneralization) elem;
                    UmlElement src = elements.get(e.getSourceId());
                    if (src.getType() == ElementType.UML_CLASS) {
                        ((UmlClass)src).addParent((UmlClass)elements.get(
                                e.getTargetid())); } else {
                        ((UmlInterface)src).addParent((UmlInterface)
                                elements.get(e.getTargetid())); }
                    break; }
                case UML_INTERFACE_REALIZATION: {
                    UmlInterfaceRealization e = (UmlInterfaceRealization) elem;
                    UmlClass src = (UmlClass) elements.get(e.getSourceId());
                    src.addInterface((UmlInterface) elements.get(
                            e.getTargetid()));
                    break; }
                default: otherFill(elem); }
        }
        associationEmbed();
    }

    private void otherFill(UmlElement elem) {
        switch (elem.getType()) {
            case UML_STATE: {
                UmlState e = (UmlState) elem;
                UmlRegion region = (UmlRegion) elements.get(e.getUmlParent());
                UmlStateMachine machine = (UmlStateMachine)
                        elements.get(region.getUmlParent());
                machine.addState(e);
                break; }
            case UML_TRANSITION: {
                UmlTransition e = (UmlTransition) elem;
                UmlRegion region = (UmlRegion) elements.get(e.getUmlParent());
                UmlStateMachine machine = (UmlStateMachine)
                        elements.get(region.getUmlParent());
                machine.addTransition();
                UmlState source = (UmlState) elements.get(e.getSourceId());
                UmlState target = (UmlState) elements.get(e.getTargetId());
                e.setSource(source);
                e.setTarget(target);
                source.addSubsequence(e);
                break; }
            case UML_LIFELINE: {
                UmlLifeline e = (UmlLifeline) elem;
                UmlInteraction in = (UmlInteraction)
                        elements.get(e.getUmlParent());
                in.addObject(e);
                UmlAttribute attr = (UmlAttribute)
                        elements.get(e.getRepresentId());
                e.setRepresent(attr);
                break; }
            case UML_MESSAGE: {
                UmlMessage e = (UmlMessage) elem;
                UmlInteraction in = (UmlInteraction)
                        elements.get(e.getUmlParent());
                in.addMessage();
                UmlInteractionObject source =
                        (UmlInteractionObject) elements.get(e.getSourceId());
                UmlInteractionObject target =
                        (UmlInteractionObject) elements.get(e.getTargetId());
                target.addIncoming(e);
                source.addSent(e);
                e.setSource(source);
                e.setTarget(target);
                break; }
            default: } }

    private void associationEmbed() {
        for (UmlAssociation association: associationTemp) {
            UmlDirectedAssociation dir1 = association.getDirected1();
            UmlDirectedAssociation dir2 = association.getDirected2();
            dir1.getSource().addAssociation(dir1);
            dir2.getSource().addAssociation(dir2); }
        for (UmlOperation e: operationTemp) {
            UmlExtendable cls = (UmlExtendable) elements.get(e.getUmlParent());
            cls.addMethod(e); } }

    private UmlClass getUmlClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        int num = classes.keySize(className);
        if (num == 0) { throw new ClassNotFoundException(className); }
        if (num > 1) { throw new ClassDuplicatedException(className); }
        return classes.get(className); }

    public int getClassCount() { return classes.realSize(); }

    public int getClassOperationCount(String className,
                                      OperationQueryType[] queryTypes)
            throws ClassNotFoundException, ClassDuplicatedException,
            ConflictQueryTypeException {
        UmlClass cls = getUmlClass(className);
        QueryEnum ret = QueryEnum.ALL;
        QueryEnum param = QueryEnum.ALL;
        for (int i = 0; i < queryTypes.length; ++i) {
            OperationQueryType qt = queryTypes[i];
            switch (qt) {
                case PARAM: {
                    if (param == QueryEnum.NO) {
                        throw new ConflictQueryTypeException(); }
                    param = QueryEnum.YES;
                    break; }
                case NON_PARAM: {
                    if (param == QueryEnum.YES) {
                        throw new ConflictQueryTypeException(); }
                    param = QueryEnum.NO;
                    break; }
                case RETURN: {
                    if (ret == QueryEnum.NO) {
                        throw new ConflictQueryTypeException(); }
                    ret = QueryEnum.YES;
                    break; }
                case NON_RETURN: {
                    if (ret == QueryEnum.YES) {
                        throw new ConflictQueryTypeException(); }
                    ret = QueryEnum.NO;
                    break; }
                default: } }
        return cls.getOperationCount(ret, param); }

    public int getClassAttributeCount(String className,
                                      AttributeQueryType queryType)
        throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass cls = getUmlClass(className);
        return cls.getPropertyCount(queryType); }

    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass cls = getUmlClass(className);
        return cls.getAssociationCount(); }

    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass cls = getUmlClass(className);
        HashSet<UmlClass> classes = cls.getAssociationList();
        ArrayList<String> res = new ArrayList<>(classes.size());
        for (UmlClass ucls: classes) {
            res.add(ucls.getName()); }
        return res; }

    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName) throws
            ClassNotFoundException, ClassDuplicatedException {
        UmlClass cls = getUmlClass(className);
        return cls.getOperationVisibility(operationName); }

    public Visibility getClassAttributeVisibility(String className,
                                                  String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        UmlClass cls = getUmlClass(className);
        return cls.getAttributeVisibility(attributeName); }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass cls = getUmlClass(className);
        return cls.getTopClassName(); }

    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass cls = getUmlClass(className);
        HashSet<UmlInterface> interfaces = cls.getImplements();
        ArrayList<String> res = new ArrayList<>(interfaces.size());
        for (UmlInterface uinterface: interfaces) {
            res.add(uinterface.getName()); }
        return res; }

    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        UmlClass cls = getUmlClass(className);
        return cls.getInfoHidden(); }

    private UmlInteraction getUmlInteraction(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        int sz = interactions.keySize(interactionName);
        if (sz == 0) {
            throw new InteractionNotFoundException(interactionName); }
        if (sz != 1) {
            throw new InteractionDuplicatedException(interactionName); }
        return interactions.get(interactionName); }

    public int getParticipantCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        UmlInteraction in = getUmlInteraction(interactionName);
        return in.queryObjectCount(); }

    public int getMessageCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        UmlInteraction in = getUmlInteraction(interactionName);
        return in.queryMessageCount(); }

    public int getIncomingMessageCount(String interactionName,
                                       String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        UmlInteraction in = getUmlInteraction(interactionName);
        return in.queryIncomingCount(lifelineName); }

    private UmlStateMachine getUmlStateMachine(String stateMachineName) throws
            StateMachineDuplicatedException, StateMachineNotFoundException {
        int sz = machines.keySize(stateMachineName);
        if (sz == 0) {
            throw new StateMachineNotFoundException(stateMachineName); }
        if (sz != 1) {
            throw new StateMachineDuplicatedException(stateMachineName); }
        return machines.get(stateMachineName); }

    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        UmlStateMachine machine = getUmlStateMachine(stateMachineName);
        return machine.queryStateCount(); }

    public int getTransitionCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        UmlStateMachine machine = getUmlStateMachine(stateMachineName);
        return machine.queryTransitionCount(); }

    public int getSubsequentStateCount(String stateMachineName,
                                       String stateName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        UmlStateMachine machine = getUmlStateMachine(stateMachineName);
        return machine.querySubsequenceCount(stateName); }

    public void checkForUml001() throws UmlRule001Exception {
        HashSet<AttributeClassInformation> info = new HashSet<>();
        for (UmlClass cls: classes.values()) {
            info.addAll(cls.getDuplicatedNames()); }
        if (info.size() > 0) { throw new UmlRule001Exception(info); } }

    public void checkForUml002() throws UmlRule002Exception {
        HashSet<com.oocourse.uml3.models.elements.UmlClassOrInterface>
                res = checker.getUmlRule002();
        if (res.size() > 0) { throw new UmlRule002Exception(res); } }

    public void checkForUml003() throws UmlRule003Exception {
        HashSet<com.oocourse.uml3.models.elements.UmlClassOrInterface>
                res = checker.getUmlRule003();
        if (res.size() > 0) { throw new UmlRule003Exception(res); } }

    public void checkForUml004() throws UmlRule004Exception {
        HashSet<com.oocourse.uml3.models.elements.UmlClassOrInterface>
                res = checker.getUmlRule004();
        if (res.size() > 0) { throw new UmlRule004Exception(res); } }

    public void checkForUml005() throws UmlRule005Exception {
        if (hasEmptyName) { throw new UmlRule005Exception(); } }

    public void checkForUml006() throws UmlRule006Exception {
        for (UmlInterface i: interfaces.values()) {
            if (i.isNotPublic()) { throw new UmlRule006Exception(); } } }

    public void checkForUml007() throws UmlRule007Exception {
        for (UmlState fin: finalStates) {
            if (fin.getSubsequenceCount() > 0) {
                throw new UmlRule007Exception(); } } }

    public void checkForUml008() throws UmlRule008Exception {
        for (UmlState init: initialStates) {
            if (init.getSubsequenceCount() > 1) {
                throw new UmlRule008Exception(); } } }
}
