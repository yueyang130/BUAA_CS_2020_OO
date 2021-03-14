import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Cloneable {
    private ArrayList<Factor> arr;

    public Term() {
        this(BigInteger.ONE, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public Term(BigInteger i0, BigInteger i1, BigInteger i2, BigInteger i3) {
        arr = new ArrayList<>();
        arr.add(FactorFactory.getFactor("const", i0));
        arr.add(FactorFactory.getFactor("pow", i1));
        arr.add(FactorFactory.getFactor("sin", i2));
        arr.add(FactorFactory.getFactor("cos", i3));
    }

    public Term(ArrayList<Factor> factors) {
        arr = factors;
    }

    public BigInteger[] getIndexs() {
        BigInteger[] indexs = new BigInteger[4];
        for (int i = 0; i < 4; i++) {
            indexs[i] = arr.get(i).getIndex();
        }
        return indexs;
    }

    public Exp diff() {
        Exp dexp = new Exp();
        for (int i = 0; i < arr.size(); i++) {
            //ArrayList<Factor> copy = (ArrayList<Factor>) arr.clone();

            ArrayList<Factor> copy = new ArrayList<>();
            for (Factor f : arr) {
                try {
                    copy.add(f.clone());
                } catch (CloneNotSupportedException e) {
                    System.out.println("cannot clone!");
                }

            }
            Factor f;

            switch (i) {
                case 0 :
                    f = copy.set(i, FactorFactory.getFactor("const", BigInteger.ONE));
                    break;
                case 1 :
                    f = copy.set(i, FactorFactory.getFactor("pow", BigInteger.ZERO));
                    break;
                case 2 :
                    f = copy.set(i, FactorFactory.getFactor("sin", BigInteger.ZERO));
                    break;
                case 3 :
                    f = copy.set(i, FactorFactory.getFactor("cos", BigInteger.ZERO));
                    break;
                default:
                    f = null;
                    System.out.println("Error type!");
            }
            Term t1 = new Term(copy);
            Term t2 = f.diff();
            dexp.add(t1.mult(t2));
        }
        return dexp;
    }

    public Term mult(Term t2) {
        // multiply two factors
        for (Factor f : t2.arr) {
            this.add(f);
        }
        return this;
    }

    public void add(Factor f) {
        // add one factor to the term
        BigInteger pre;
        if (f instanceof Const) {
            pre = arr.get(0).getIndex();
            arr.get(0).setIndex(pre.multiply(f.getIndex()));
        } else if (f instanceof  Pow) {
            pre = arr.get(1).getIndex();
            arr.get(1).setIndex(pre.add(f.getIndex()));
        } else if (f instanceof Sin) {
            pre = arr.get(2).getIndex();
            arr.get(2).setIndex(pre.add(f.getIndex()));
        } else if (f instanceof Cos) {
            pre = arr.get(3).getIndex();
            arr.get(3).setIndex(pre.add(f.getIndex()));
        }
    }

    public Triple getTriple() {
        BigInteger[] indexs = getIndexs();
        return new Triple(indexs[1], indexs[2], indexs[3]);
    }

    public void Merge(Term t2) {
        // should be used when only has the same hashcode
        BigInteger pre = arr.get(0).getIndex();
        arr.get(0).setIndex(pre.add(t2.arr.get(0).getIndex()));
    }

}
