import java.math.BigInteger;
import java.util.ArrayList;

public class Pow extends Factor {
    private BigInteger index;

    public Pow(BigInteger i) {
        index = i;
    }

    public Term diff() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(FactorFactory.getFactor("const", index));
        factors.add(FactorFactory.getFactor("pow", index.subtract(BigInteger.ONE)));
        return new Term(factors);
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    public BigInteger getIndex() {
        return index;
    }

    public Pow clone() throws CloneNotSupportedException {
        Pow clone = (Pow) super.clone();
        clone.index = new BigInteger(index.toString());
        return clone;
    }
}
