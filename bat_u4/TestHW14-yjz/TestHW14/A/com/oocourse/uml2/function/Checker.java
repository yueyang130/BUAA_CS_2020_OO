package com.oocourse.uml2.function;

import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.HashSet;

public class Checker {
    public static void checkOperationQueryType(HashSet<OperationQueryType> queryTypeSet)
        throws ConflictQueryTypeException {
        if (queryTypeSet.contains(OperationQueryType.NON_RETURN) &&
            queryTypeSet.contains(OperationQueryType.RETURN)) {
            throw new ConflictQueryTypeException();
        }
        if (queryTypeSet.contains(OperationQueryType.NON_PARAM) &&
            queryTypeSet.contains(OperationQueryType.PARAM)) {
            throw new ConflictQueryTypeException();
        }
    }

    public static boolean isStateElement(UmlElement element) {
        //include initial state and final state
        if (element.getElementType() == ElementType.UML_STATE) {
            return true;
        }
        if (element.getElementType() == ElementType.UML_PSEUDOSTATE) {
            return true;
        }
        if (element.getElementType() == ElementType.UML_FINAL_STATE) {
            return true;
        }
        return false;
    }
}
