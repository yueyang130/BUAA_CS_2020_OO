import com.oocourse.uml3.models.elements.UmlClassOrInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class UIntChecker {
    private MultiMap<String, UmlClass> classes;
    private MultiMap<String, UmlInterface> interfaces;
    private HashSet<UmlClassOrInterface> umlRule002res;
    private HashSet<UmlClassOrInterface> umlRule003res;
    private HashSet<UmlClassOrInterface> umlRule004res;

    public UIntChecker(MultiMap<String, UmlClass> classes,
                       MultiMap<String, UmlInterface> interfaces) {
        this.classes = classes;
        this.interfaces = interfaces;
        //umlRule002res = new HashSet<>();
        init();
        check();
    }

    private HashMap<String, UmlClass> topClasses;
    private HashMap<String, UmlInterface> topInterfaces;
    private HashMap<String, UmlExtendable> elements;

    private void init() {
        topClasses = new HashMap<>();
        topInterfaces = new HashMap<>();
        elements = new HashMap<>();
        for (UmlClass cls: classes.values()) {
            if (cls.getParentCount() == 0) {
                topClasses.put(cls.getId(), cls); }
            elements.put(cls.getId(), cls);
        }
        for (UmlInterface cls: interfaces.values()) {
            if (cls.getParentCount() == 0) {
                topInterfaces.put(cls.getId(), cls); }
            elements.put(cls.getId(), cls);
        }
    }

    private void check() {
        checkCircuit();
        umlRule003res = new HashSet<>();
        umlRule004res = new HashSet<>();
        if (umlRule002res.size() == 0) {
            checkDuplicate(); }
    }

    private static class Tarjan {
        private HashMap<String, Integer> dfn;
        private HashMap<String, Integer> low;
        private LinkedList<String> stack;
        private int count;
        private HashSet<UmlClassOrInterface> res;

        private Tarjan() {
            dfn = new HashMap<>();
            low = new HashMap<>();
            stack = new LinkedList<>();
            res = new HashSet<>();
            count = 0;
        }

        private int dfn(String id) {
            return dfn.getOrDefault(id, 0);
        }

        private int dfn(String id, int value) {
            dfn.put(id, value);
            return value;
        }

        private int low(String id) {
            return low.getOrDefault(id, 0);
        }

        private int low(String id, int value) {
            low.put(id, value);
            return value;
        }
    }

    private UmlExtendable element(String id) {
        return elements.get(id);
    }

    private void checkCircuit() {
        Tarjan data = new Tarjan();
        for (Map.Entry<String, UmlExtendable> e: elements.entrySet()) {
            String id = e.getKey();
            if (data.dfn(id) == 0) {
                tarjan(id, data);
            }
        }
        umlRule002res = data.res;
    }

    private void tarjan(String id, Tarjan data) {
        data.low(id, data.dfn(id, ++data.count));
        UmlExtendable current = element(id);
        data.stack.add(id);
        for (String parent: current.getUmlParents().keySet()) {
            if (parent.equals(id)) {
                data.res.add((UmlClassOrInterface) current.getOriginElement());
                continue;
            }
            int pdfn = data.dfn(parent);
            if (pdfn < 0) { continue; }
            if (pdfn == 0) { tarjan(parent, data); }
            data.low(id, Math.min(data.low(id), data.low(parent)));
        }
        if (data.dfn(id) == data.low(id)) {
            HashSet<com.oocourse.uml3.models.elements.UmlClassOrInterface>
                    component = new HashSet<>();
            String top;
            do {
                top = data.stack.removeLast();
                data.dfn(top, -1);
                component.add((UmlClassOrInterface)
                        element(top).getOriginElement());
            } while (!id.equals(top));
            if (component.size() > 1) { data.res.addAll(component); }
        }
    }

    private void checkDuplicate() {
        for (String id: topInterfaces.keySet()) {
            HashMap<String, Integer> visited = new HashMap<>();
            duplicateDfs(id, visited, umlRule004res, umlRule003res);
        }
    }

    private void duplicateDfs(String id, HashMap<String, Integer> visited,
            HashSet<UmlClassOrInterface> resClass,
            HashSet<UmlClassOrInterface> resInterface) {
        int vis = visited.getOrDefault(id, 0) + 1;
        visited.put(id, vis);
        UmlExtendable elem = element(id);
        if (vis > 1) {
            if (elem instanceof UmlClass) {
                resClass.add((UmlClassOrInterface)elem.getOriginElement());
            } else {
                resInterface.add((UmlClassOrInterface)elem.getOriginElement());
            }
        }
        for (Map.Entry<String, LinkedList<UmlExtendable>> e:
                elem.getChildren().entrySet()) {
            String child = e.getKey();
            int sz = e.getValue().size();
            for (int i = 0; i < sz; ++i) {
                vis = visited.getOrDefault(child, 0);
                if (vis > 1) { break; } // optimization
                duplicateDfs(child, visited, resClass, resInterface);
            }
        }
    }

    public HashSet<com.oocourse.uml3.models.elements.UmlClassOrInterface>
            getUmlRule002() { return umlRule002res; }

    public HashSet<com.oocourse.uml3.models.elements.UmlClassOrInterface>
            getUmlRule003() { return umlRule003res; }

    public HashSet<com.oocourse.uml3.models.elements.UmlClassOrInterface>
            getUmlRule004() { return umlRule004res; }
}
