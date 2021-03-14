import java.math.BigInteger;

public class Const extends Factor {
    private BigInteger index;

    public Const(BigInteger v) {
        index = v;
    }

    public Factor diff() {
        return FactorFactory.getFactor("const", null, BigInteger.ZERO);
    }

    public void setIndex(BigInteger value) {
        this.index = value;
    }

    public BigInteger getIndex() { return index; }

    public Const clone() throws CloneNotSupportedException {
        Const clone = (Const) super.clone();
        clone.index = new BigInteger(index.toString());
        return clone;
    }

    @Override
    public String toString() {
        return index.toString();
    }
}
