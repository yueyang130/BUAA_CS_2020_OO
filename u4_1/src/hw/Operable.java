package hw;

import com.oocourse.uml1.models.elements.UmlAttribute;

public interface Operable {
    void addAttribute(UmlAttribute attr);

    void addOperation(MyOperation op);
}
