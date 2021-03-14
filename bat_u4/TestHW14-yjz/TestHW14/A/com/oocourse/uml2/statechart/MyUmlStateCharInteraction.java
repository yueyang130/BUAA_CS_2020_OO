package com.oocourse.uml2.statechart;

import com.oocourse.uml2.general.ElementInformation;
import com.oocourse.uml2.general.Node;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlStateMachine;

import java.util.HashSet;
import java.util.Stack;

public class MyUmlStateCharInteraction implements UmlStateChartInteraction {
    private ElementInformation eleInfo;

    public MyUmlStateCharInteraction(ElementInformation eleInfo) {
        this.eleInfo = eleInfo;
    }

    private UmlStateMachine getStateMachineElement(String stateMachineName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        UmlStateMachine stateMachineElement = null;
        for (UmlStateMachine tempStateMachineElement : eleInfo.getStateMachineElements()) {
            String tempStateMachineName = tempStateMachineElement.getName();
            if (tempStateMachineName != null) {
                if (tempStateMachineName.equals(stateMachineName)) {
                    if (stateMachineElement == null) {
                        stateMachineElement = tempStateMachineElement;
                    } else {
                        throw new StateMachineDuplicatedException(stateMachineName);
                    }
                }
            }
        }
        if (stateMachineElement == null) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        return stateMachineElement;
    }

    private UmlElement getStateElement(UmlStateMachine stateMachineElement, String stateName)
        throws StateNotFoundException, StateDuplicatedException {
        Node regionNode = getRegionNode(stateMachineElement);
        HashSet<String> stateChildIds = regionNode.getStateChildIds();
        UmlElement stateElement = null;
        for (String stateChildId: stateChildIds) {
            UmlElement stateChildElement = eleInfo.getElementMap().get(stateChildId);
            String stateChildName = stateChildElement.getName();
            if (stateChildName != null) {
                if (stateChildName.equals(stateName)) {
                    if (stateElement == null) {
                        stateElement = stateChildElement;
                    } else {
                        throw new StateDuplicatedException(stateMachineElement.getName(),
                            stateName);
                    }
                }
            }
        }
        if (stateElement == null) {
            throw new StateNotFoundException(stateMachineElement.getName(), stateName);
        }
        return stateElement;
    }

    private Node getRegionNode(UmlStateMachine stateMachineElement) {
        Node stateMachineNode = eleInfo.getIdTree().get(stateMachineElement.getId());
        String regionChildId = stateMachineNode.getRegionChildId();
        Node regionNode = eleInfo.getIdTree().get(regionChildId);
        return regionNode;
    }

    @Override
    public int getStateCount(String stateMachineName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        UmlStateMachine stateMachineElement = getStateMachineElement(stateMachineName);
        return getRegionNode(stateMachineElement).getStateChildIds().size();
    }

    @Override
    public int getTransitionCount(String stateMachineName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        UmlStateMachine stateMachineElement = getStateMachineElement(stateMachineName);
        return getRegionNode(stateMachineElement).getTransitionChildIds().size();
    }

    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException,
        StateNotFoundException, StateDuplicatedException {
        UmlStateMachine stateMachineElement = getStateMachineElement(stateMachineName);
        UmlElement stateElement = getStateElement(stateMachineElement, stateName);

        //bfs
        boolean circleFlag = false;
        HashSet<String> visited = new HashSet<String>();
        Stack<String> stateIds = new Stack<String>();
        stateIds.push(stateElement.getId());
        while (true) {
            String stateId = stateIds.pop();
            visited.add(stateId);
            HashSet<String> subsequentStateIds = eleInfo.getTransitionPic().get(stateId);
            if (subsequentStateIds != null) {
                if (subsequentStateIds.contains(stateElement.getId())) {
                    circleFlag = true;
                }
                for (String subsequentStateId : subsequentStateIds) {
                    if (!visited.contains(subsequentStateId)) {
                        stateIds.push(subsequentStateId);
                    }
                }
            }
            if (stateIds.empty()) {
                break;
            }
        }

        int ret = visited.size();
        if (circleFlag == false) {
            --ret;
        }
        return ret;
    }
}
