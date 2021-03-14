package com.oocourse.uml2.function;

import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Initialer {
    public static void initAssociatePic(HashMap<String, UmlElement> elementMap,
        HashSet<UmlAssociation> associationElements,
        HashMap<String, ArrayList<String>> associatePic) {
        for (UmlAssociation associationElement : associationElements) {
            String end1 = associationElement.getEnd1();
            String end2 = associationElement.getEnd2();
            String sourceId = ((UmlAssociationEnd) elementMap.get(end1)).getReference();
            String targetId = ((UmlAssociationEnd) elementMap.get(end2)).getReference();
            for (int i = 1; i <= 2; i++) {
                if (i == 2) {
                    String tempId = sourceId;
                    sourceId = targetId;
                    targetId = tempId;
                }
                if (associatePic.containsKey(sourceId)) {
                    associatePic.get(sourceId).add(targetId);
                } else {
                    ArrayList<String> targetIds = new ArrayList<String>();
                    targetIds.add(targetId);
                    associatePic.put(sourceId, targetIds);
                }
            }
        }
    }

    public static void initOperationQueryInfo(HashMap<OperationQueryType,
        Boolean> operationQueryInfo) {
        operationQueryInfo.put(OperationQueryType.NON_RETURN, true);
        operationQueryInfo.put(OperationQueryType.RETURN, false);
        operationQueryInfo.put(OperationQueryType.NON_PARAM, true);
        operationQueryInfo.put(OperationQueryType.PARAM, false);
    }

    public static void initVisibilityInfo(HashMap<Visibility, Integer> visibilityInfo) {
        visibilityInfo.put(Visibility.PUBLIC, 0);
        visibilityInfo.put(Visibility.PROTECTED, 0);
        visibilityInfo.put(Visibility.PRIVATE, 0);
        visibilityInfo.put(Visibility.PACKAGE, 0);
    }
}
