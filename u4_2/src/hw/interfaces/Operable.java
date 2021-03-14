package hw.interfaces;

import com.oocourse.uml2.models.elements.UmlAttribute;
import hw.elements.MyOperation;

public interface Operable {
    void addAttribute(UmlAttribute attr);

    void addOperation(MyOperation op);
}
