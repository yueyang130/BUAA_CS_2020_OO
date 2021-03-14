import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Cloneable {
    private ArrayList<Factor> arr;

    public Term() {
        arr = new ArrayList<>();
    }

    public Term(ArrayList<Factor> factors) {
        this();
        arr.addAll(factors);
    }

    public Term(Factor factor) {
        this();
        arr.add(factor);
    }

    public ArrayList<Factor> getArr() {
        return clone().arr;
    }

    public Exp diff() {
        Exp dexp = new Exp();
        for (int i = 0; i < arr.size(); i++) {
            //ArrayList<Factor> copy = (ArrayList<Factor>) arr.clone();
            Term t1 = this.clone();
            Factor f = t1.arr.remove(i);
            Factor t2 = f.diff();
            dexp.add(t1.mult(t2));
        }
        return dexp;
    }

    public Term mult(Factor t2) {
        this.add(t2);
        return this;
    }

    public void add(Factor f) {
        // add one factor to the term
        arr.add(f);
    }

    public void Merge(Term t2) {
        // should be used when only has the same hashcode
        BigInteger pre = arr.get(0).getIndex();
        arr.get(0).setIndex(pre.add(t2.arr.get(0).getIndex()));
    }

    public void recursionSimplify() {
        //         recursion to simplify exp in TriF and expfactor
        for (int i = arr.size() - 1; i >= 0; i--) {
            Factor f = arr.get(i);
            if (f instanceof Sin) {
                ((Sin) f).simplify();
            } else if (f instanceof Cos) {
                ((Cos) f).simplify();
            } else if (f instanceof ExpFactor) {
                ((ExpFactor) f).getExp().simplify();
            }

        }
        simplify();
    }

    public void simplify() {
        //        delete expfactor's bracket
        //        在for中remove时使用倒序循环，避免跳项
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = arr.size() - 1; i >= 0; i--) {
                Factor f = arr.get(i);
                if (f instanceof ExpFactor) {
                    ArrayList<Term> terms = ((ExpFactor) f).getExp().getTerms();
                    if (terms.size() == 1) {
                        arr.remove(i);
                        arr.addAll(terms.get(0).arr);
                        flag = true;
                    }
                    if (terms.size() == 0) {
                        arr.set(i, FactorFactory.getFactor("const", null, BigInteger.ZERO));
                    }
                }
            }
        }
        // merge const in a term
        BigInteger product = BigInteger.ONE;
        for (int i = arr.size() - 1; i >= 0; i--) {
            Factor f = arr.get(i);
            if (f instanceof  Const) {
                product = ((Const) f).getIndex().multiply(product);
                arr.remove(i);
            }
        }
        arr.add(0, FactorFactory.getFactor("const", null, product));
        if (product.equals(BigInteger.ZERO)) { return; }
        // merge pow in a term
        BigInteger sum = BigInteger.ZERO;
        for (int i = arr.size() - 1; i >= 0; i--) {
            Factor f = arr.get(i);
            if (f instanceof Pow) {
                sum = ((Pow) f).getIndex().add(sum);
                arr.remove(i);
            }
        }
        arr.add(1, FactorFactory.getFactor("pow", null, sum));
    }

    @Override
    public Term clone() {
        ArrayList<Factor> newFactors = new ArrayList<>();
        for (Factor f : arr) {
            try {
                newFactors.add(f.clone());
            } catch (CloneNotSupportedException e) {
                System.out.println("cannot clone!");
            }
        }
        Term copy = new Term(newFactors);
        return copy;
    }

    @Override
    public String toString() {
        String str = "";
        int start;
        Factor f0 = arr.get(0);
        if (f0 instanceof Const && arr.size() > 1) {
            BigInteger c = f0.getIndex();
            if (c.equals(BigInteger.ONE.negate())) {
                str += "-";
            } else if (!c.equals(BigInteger.ONE)) {
                str += c + "*";
            }
        } else { str += f0 + "*"; }

        Factor f1 = arr.get(1);
        if (f1 instanceof Pow && arr.size() > 2
                && f1.toString().equals("1")) {
            str += "";
        } else { str += f1 + "*"; }


        for (start = 2; start < arr.size(); start++) {
            String factor = arr.get(start).toString();
            str += factor;
            if (!factor.equals("")) {
                str +=  "*";
            }
        }
        // remove the last '*'
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }
}
