package hw.elements;

import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlInterface;
import hw.interfaces.AssociatedItem;
import hw.interfaces.Operable;

import java.util.ArrayList;
import java.util.List;

public class MyInterface implements AssociatedItem, Operable {
    private List<MyInterface> superInterf = new ArrayList<>();
    private UmlInterface umlInterf;
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
    public void addAssociatedEnd(AssociatedItem end) {
        //TODO
    }

    @Override
    public void addAttribute(UmlAttribute attr) {
        //TODO
    }

    @Override
    public void addOperation(MyOperation op) {
        //TODO
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
