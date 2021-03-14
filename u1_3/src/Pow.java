import java.math.BigInteger;
import java.util.ArrayList;

public class Pow extends Factor {
    private BigInteger index;

    public Pow(BigInteger i) {
        index = i;
    }

    public Factor diff() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(FactorFactory.getFactor("const", null, index));
        factors.add(FactorFactory.getFactor("pow", null, index.subtract(BigInteger.ONE)));
        Exp diffexp = new Exp(new Term(factors));
        return new ExpFactor(diffexp);
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

    @Override
    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return "1";
        }
        if (index.equals(BigInteger.ONE)) {
            return "x";
        }
        return "x**" + index;
    }
}
