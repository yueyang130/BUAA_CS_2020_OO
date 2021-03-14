package hw.interfaces;

import com.oocourse.uml3.models.elements.UmlAssociationEnd;

public interface AssociatedItem  {

    void addAssociatedItem(AssociatedItem target);

    void addAssociationEnd(UmlAssociationEnd end);

    boolean equals(Object obj);
}
