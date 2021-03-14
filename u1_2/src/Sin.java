import java.math.BigInteger;
import java.util.ArrayList;

public class Sin extends Factor {
    private BigInteger index;

    public Sin(BigInteger i) {
        index = i;
    }

    public Term diff() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(FactorFactory.getFactor("const", index));
        factors.add(FactorFactory.getFactor("sin", index.subtract(BigInteger.ONE)));
        factors.add(FactorFactory.getFactor("cos", BigInteger.ONE));
        return new Term(factors);
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    public BigInteger getIndex() {
        return index;
    }

    public Sin clone() throws CloneNotSupportedException {
        Sin clone = (Sin) super.clone();
        clone.index = new BigInteger(index.toString());
        return clone;
    }
}