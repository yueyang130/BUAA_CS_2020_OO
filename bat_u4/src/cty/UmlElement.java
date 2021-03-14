import com.oocourse.uml3.models.common.ElementType;

import java.util.Objects;

public abstract class UmlElement {
    private String id;
    private String name;
    private String umlparent;
    private com.oocourse.uml3.models.elements.UmlElement element
            = null;

    public UmlElement(String id) {
        this(id, null, null);
    }

    public UmlElement(String id, String parent) {
        this(id, null, parent);
    }

    public UmlElement(com.oocourse.uml3.models.elements.UmlElement element) {
        this.id = element.getId();
        this.umlparent = element.getParentId();
        this.name = element.getName();
        this.element = element;
    }

    public UmlElement(String id, String name, String parent) {
        this.id = id;
        this.name = name;
        this.umlparent = parent;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getUmlParent() { return umlparent; }

    public abstract ElementType getType();

    public void setName(String name) { this.name = name; }

    public void setUmlParent(String parent) { this.umlparent = parent; }

    public com.oocourse.uml3.models.elements.UmlElement
        getOriginElement() { return element; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        UmlElement that = (UmlElement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
