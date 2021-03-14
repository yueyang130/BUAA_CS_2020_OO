package hw.elements;

import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.List;

public class MyOperation {
    private UmlOperation umlOp;
    // 将输入参数和返回值分开存储
    private List<UmlParameter> params = new ArrayList<>();
    private List<UmlParameter> returnValues = new ArrayList<>();

    public MyOperation(UmlOperation umlOp) {
        this.umlOp = umlOp;
    }

    public String getId() {
        return umlOp.getId();
    }

    public String getName() {
        return umlOp.getName();
    }

    public Visibility getVisibility() {
        return umlOp.getVisibility();
    }

    public boolean hasReturn() {
        return !returnValues.isEmpty();

    }

    public boolean hasParam() {
        return !params.isEmpty();
    }

    public void addParam(UmlParameter param) {
        if (param.getDirection() == Direction.RETURN) {
            returnValues.add(param);
            return;
        }
        if (param.getDirection() == Direction.IN) {
            params.add(param);
            return;
        }
        System.out.println("In java, param can be only in or return");
        System.exit(1);
    }

    /**
     * check op and params of non-return
     * @throws UmlRule005Exception
     */
    public void checkOpElemName() throws UmlRule005Exception {
        if (getName() == null) {
            throw new UmlRule005Exception();
        }
        for (UmlParameter param : params) {
            if (param.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
    }

    //public void addReturnValue(UmlParameter param) {
    //    returnValues.add(param);
    //}

}
