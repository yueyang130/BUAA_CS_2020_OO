import java.math.BigInteger;
import java.util.ArrayList;

public class Const extends Factor {
    private BigInteger index;

    public Const(BigInteger v) {
        index = v;
    }

    public Term diff() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(FactorFactory.getFactor("const", BigInteger.ZERO));
        return new Term(factors);
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
}
