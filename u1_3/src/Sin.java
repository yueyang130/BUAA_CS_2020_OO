import java.math.BigInteger;
import java.util.ArrayList;

public class Sin extends Factor {
    private BigInteger index;
    private Factor factor;

    public Sin(Factor f, BigInteger i) {
        factor = f;
        index = i;
    }

    public Factor diff() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(FactorFactory.getFactor("const", null, index));
        factors.add(FactorFactory.getFactor("sin", factor, index.subtract(BigInteger.ONE)));
        factors.add(FactorFactory.getFactor("cos", factor, BigInteger.ONE));
        factors.add(factor.diff());
        Exp diffexp = new Exp(new Term(factors));
        return new ExpFactor(diffexp);
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    public BigInteger getIndex() {
        return index;
    }

    public Factor getFactor() {
        return factor;
    }

    public void simplify() {
        Factor f = factor;
        if (f instanceof Sin) {
            ((Sin) f).simplify();
        } else if (f instanceof Cos) {
            ((Cos) f).simplify();
        } else if (f instanceof ExpFactor) {
            ((ExpFactor) f).getExp().simplify();
        }
    }

    public Sin clone() throws CloneNotSupportedException {
        Sin clone = (Sin) super.clone();
        clone.index = new BigInteger(index.toString());
        return clone;
    }

    @Override
    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return "1";
        }
        if (index.equals(BigInteger.ONE)) {
            return "sin(" + factor.toString() + ")";
        }
        return "sin(" + factor.toString() + ")" + "**" + index;
    }
}