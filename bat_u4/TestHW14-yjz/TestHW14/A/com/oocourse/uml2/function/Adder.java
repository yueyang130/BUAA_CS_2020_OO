package com.oocourse.uml2.function;

import com.oocourse.uml2.general.Node;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Adder {

    public static void addRelation2SetPic(String sourceId, String targetId,
                                       HashMap<String, HashSet<String>> pic) {
        if (pic.containsKey(sourceId)) {
            pic.get(sourceId).add(targetId);
        } else {
            HashSet<String> targetIds = new HashSet<String>();
            targetIds.add(targetId);
            pic.put(sourceId, targetIds);
        }
    }

    public static void addRelation2ListPic(String sourceId, String targetId,
                                        HashMap<String, ArrayList<String>> pic) {
        if (pic.containsKey(sourceId)) {
            pic.get(sourceId).add(targetId);
        } else {
            ArrayList<String> targetIds = new ArrayList<String>();
            targetIds.add(targetId);
            pic.put(sourceId, targetIds);
        }
    }

    //保证与id相对应的node存在
    //保证node的parentId正确
    //更新parent node的childIds与specific childIds
    public static void addElement2IdTree(UmlElement element, HashMap<String, Node> idTree) {
        String id = element.getId();
        String parentId = element.getParentId();
        Node node = idTree.get(id);
        if (node != null) {
            node.setParentId(parentId);
        } else {
            node = new Node(id, parentId);
            idTree.put(id, node);
        }
        if (element.getElementType() != ElementType.UML_CLASS &&
            element.getElementType() != ElementType.UML_INTERFACE &&
            element.getElementType() != ElementType.UML_INTERACTION) {
            Node parentNode = idTree.get(parentId);
            if (parentNode != null) {
                parentNode.addChild(element);
            } else {
                parentNode = new Node(parentId);
                parentNode.addChild(element);
                idTree.put(parentId, parentNode);
            }
        }
    }
}
