
import java.math.BigInteger;

public abstract class Factor implements Cloneable {
    public abstract Term diff();

    public abstract void setIndex(BigInteger index);

    public abstract BigInteger getIndex();

    public  Factor clone() throws CloneNotSupportedException {
        return (Factor) super.clone();
    }
}
