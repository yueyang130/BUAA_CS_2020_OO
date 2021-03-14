public abstract class UmlExtendable extends UmlElement {
    private MultiMap<String, UmlExtendable> children;
    private MultiMap<String, UmlExtendable> parents;

    public UmlExtendable(
            com.oocourse.uml3.models.elements.UmlElement element) {
        super(element);
        children = new MultiMap<>();
        parents = new MultiMap<>();
    }

    public abstract void addMethod(UmlOperation method);

    public abstract void addAssociation(UmlDirectedAssociation da);

    private void addChild(UmlExtendable child) {
        children.put(child.getId(), child);
    }

    public MultiMap<String, UmlExtendable> getChildren() {
        return children;
    }

    public int getChildrenCount() { return children.size(); }

    public void addUmlParent(UmlExtendable parent) {
        parents.put(parent.getId(), parent);
        parent.addChild(this);
    }

    public MultiMap<String, UmlExtendable> getUmlParents() {
        return parents;
    }

    public int getParentCount() { return parents.size(); }
}
