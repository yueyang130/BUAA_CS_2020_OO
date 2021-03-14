import java.math.BigInteger;

public class ExpFactor extends Factor {
    private Exp exp = new Exp();

    public ExpFactor(Exp e) {
        exp = e;
    }

    public Factor diff() {
        return new ExpFactor(exp.diff());
    }

    public Exp getExp() {
        return exp;
    }

    @Override
    public void setIndex(BigInteger index) { }

    @Override
    public BigInteger getIndex() {
        return null;
    }

    public ExpFactor clone() {
        return new ExpFactor(exp.clone());
    }

    @Override
    public String toString() {
        //        if (exp.toString().equals("0")) {
        //            exp = new Exp(
        //                    new Term(FactorFactory.getFactor("const", null, BigInteger.ZERO)));
        //        }
        return "(" + exp.toString() + ")";
    }
}
