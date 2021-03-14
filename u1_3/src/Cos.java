import java.math.BigInteger;
import java.util.ArrayList;

public class Cos extends Factor {
    private BigInteger index;
    private Factor factor;

    public Cos(Factor f, BigInteger i) {
        factor = f;
        index = i;
    }

    public ExpFactor diff() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(FactorFactory.getFactor("const", null, index.negate()));
        factors.add(FactorFactory.getFactor("cos", factor, index.subtract(BigInteger.ONE)));
        factors.add(FactorFactory.getFactor("sin", factor, BigInteger.ONE));
        factors.add(factor.diff());
        Exp diffexp =  new Exp(new Term(factors));
        return new ExpFactor(diffexp);
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

    @Override
    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return "1";
        }
        if (index.equals(BigInteger.ONE)) {
            return "cos(" + factor.toString() + ")";
        }
        return "cos(" + factor.toString() + ")" + "**" + index;
    }
}
