import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;

import java.util.HashMap;
import java.util.HashSet;

public class UmlInterface extends UmlExtendable {
    private Visibility visibility;
    private MultiMap<String, UmlOperation> methods;
    private MultiMap<String, UmlAttribute> properties;
    private HashMap<String, UmlDirectedAssociation> associations;
    private boolean nonPublic = false;

    public UmlInterface(com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        com.oocourse.uml3.models.elements.UmlInterface e =
                (com.oocourse.uml3.models.elements.UmlInterface) element;
        this.visibility = e.getVisibility();
        this.methods = new MultiMap<>();
        this.properties = new MultiMap<>();
        this.associations = new HashMap<>();
    }

    public ElementType getType() { return ElementType.UML_INTERFACE; }

    public Visibility getVisibility() { return visibility; }

    @Override
    public void addMethod(UmlOperation method) {
        methods.put(method.getName(), method);
        if (method.getVisibility() != Visibility.PUBLIC) {
            nonPublic = true;
        }
    }

    public void addProperty(UmlAttribute attr) {
        properties.put(attr.getName(), attr);
        if (attr.getVisibility() != Visibility.PUBLIC) {
            nonPublic = true;
        }
    }

    public boolean isNotPublic() { return nonPublic; }

    @Override
    public void addAssociation(UmlDirectedAssociation da) {
        associations.put(da.getId(), da);
    }

    public void addParent(UmlInterface parent) {
        //parents.put(parent.getName(), parent);
        addUmlParent(parent);
    }

    public HashSet<UmlInterface> getParents() {
        HashSet<UmlInterface> res = new HashSet<>();
        for (UmlExtendable parent: getUmlParents().values()) {
            res.add((UmlInterface) parent);
            res.addAll(((UmlInterface) parent).getParents());
        }
        return res;
    }

    public HashSet<UmlInterface> getDirectParents() {
        HashSet<UmlInterface> res = new HashSet<>();
        for (UmlExtendable parent: getUmlParents().values()) {
            res.add((UmlInterface) parent);
        }
        return res;
    }

}
