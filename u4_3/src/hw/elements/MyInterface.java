package hw.elements;

import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterface;
import hw.interfaces.AssociatedItem;
import hw.interfaces.Operable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class MyInterface implements AssociatedItem, Operable {
    private static int Capacity = 64;
    private List<MyInterface> superInterf = new ArrayList<>();
    private UmlInterface umlInterf;
    private List<MyOperation> operations = new ArrayList<>();
    private List<UmlAttribute> attributes = new ArrayList<>();
    private List<MyClass> associationClass = new ArrayList<>();
    private List<MyInterface> associationInterfs = new ArrayList<>();

    public MyInterface(UmlInterface umlInterf) {
        this.umlInterf = umlInterf;
    }

    public String getName() {
        return umlInterf.getName();
    }

    public void setSuperInterf(MyInterface superInterf) {
        this.superInterf.add(superInterf);
    }

    public List<MyInterface> getSuperInterf() {
        return superInterf;
    }

    @Override
    public void addAssociatedItem(AssociatedItem end) {
        //TODO
    }

    @Override
    public void addAssociationEnd(UmlAssociationEnd end) {
        //TODO
    }

    @Override
    public void addAttribute(UmlAttribute attr) {
        attributes.add(attr);
    }

    @Override
    public void addOperation(MyOperation op) {
        operations.add(op);
    }

    public void checkElemName() throws UmlRule005Exception {
        // class, attr
        // op, param of non-return
        if (getName() == null) {
            throw new UmlRule005Exception();
        }
        for (UmlAttribute attr : attributes) {
            if (attr.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        for (MyOperation op : operations) {
            op.checkOpElemName();
        }

    }

    /*
    private void DFS(MyInterface current, Set<MyInterface> visited,
                     List<MyInterface> path, Set<UmlInterface> circularSet) {
        visited.add(current);
        for (MyInterface nextInterf : current.superInterf) {
            if (!visited.contains(nextInterf)) {
                DFS(nextInterf, visited, path, circularSet);
            }
        }
    }
    */

    public boolean checkCircularInheritance() {
        // dfs会走过所有以此节点开头路径
        // 通过DFS判断此点是否在环路上即可
        // stack
        Stack<MyInterface> stack = new Stack<>();
        Stack<Integer> indexStack = new Stack<>();
        stack.add(this);
        indexStack.add(0);
        // visited
        Set<MyInterface> visited = new HashSet<>(Capacity);
        visited.add(this);


        while (!stack.isEmpty()) {
            int i = indexStack.pop();
            MyInterface currInterf = stack.pop();
            if (i < currInterf.superInterf.size()) {
                MyInterface nextInterf = currInterf.superInterf.get(i);
                if (nextInterf == this) {
                    return true;
                }
                indexStack.push(i + 1);
                stack.push(currInterf);
                if (visited.add(nextInterf)) {
                    stack.add(nextInterf);
                    indexStack.add(0);
                }
            }
        }
        return false;
    }

    public boolean checkDuplicatedGeneralization() {
        Set<MyInterface> generalizedInterfSet = new HashSet<>();
        Queue<MyInterface> queue = new LinkedList<>(superInterf);
        while (!queue.isEmpty()) {
            MyInterface current = queue.poll();
            if (!generalizedInterfSet.add(current)) {
                return false;
            } else {
                queue.addAll(current.superInterf);
            }
        }
        return true;
    }

    public UmlInterface getUmlInterf() { return umlInterf; }

    public void checkAttrVisibility() throws UmlRule006Exception {
        for (UmlAttribute attr : attributes) {
            if (attr.getVisibility() != Visibility.PUBLIC) {
                throw new UmlRule006Exception();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MyInterface)) {
            return false;
        }
        MyInterface myInterf = (MyInterface) obj;
        return umlInterf.equals(myInterf.umlInterf);
    }

    @Override
    public int hashCode() {
        return umlInterf.hashCode();
    }
}
