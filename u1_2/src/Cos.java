import java.math.BigInteger;
import java.util.ArrayList;

public class Cos extends Factor {
    private BigInteger index;

    public Cos(BigInteger i) {
        index = i;
    }

    public Term diff() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(FactorFactory.getFactor("const", index.negate()));
        factors.add(FactorFactory.getFactor("cos", index.subtract(BigInteger.ONE)));
        factors.add(FactorFactory.getFactor("sin", BigInteger.ONE));
        return new Term(factors);
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    @Override    public BigInteger getIndex() {
        return index;
    }

    public Cos clone() throws CloneNotSupportedException {
        Cos clone = (Cos) super.clone();
        clone.index = new BigInteger(index.toString());
        return clone;
    }
}
