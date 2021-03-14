import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Exp {
    private ArrayList<Term> terms;

    public Exp() {
        terms = new ArrayList<>();
    }

    public Exp(Term t) {
        this();
        add(t);
    }

    public Exp diff() {
        Exp dexp = new Exp();
        for (Term t : terms) {
            dexp.add(t.diff());
        }
        return dexp;
    }

    public void add(Exp exp) {
        for (Term t : exp.terms) {
            add(t);
        }
    }

    public void add(Term t) {
        // 不添加系数为0的项
        if (!t.getIndexs()[0].equals(BigInteger.ZERO)) {
            terms.add(t);
        }

    }

    @Override
    public String toString() {
        // print output
        StringBuilder outbuilder = new StringBuilder();
        //  是否是第一个系数大于0的项
        boolean first = true;
        for (Term t : terms) {
            BigInteger[] indexs = t.getIndexs();
            BigInteger c = indexs[0];
            String term = "";
            String[] type = {"const", "x", "sin(x)", "cos(x)"};

            boolean nothasVariable = true;
            for (int i = 1; i < 4; i++) {
                nothasVariable = nothasVariable && (indexs[i].equals(BigInteger.ZERO));
            }

            if (c.equals(BigInteger.ZERO)) {
                continue;
            }

            if (nothasVariable) {
                term += c;
            } else {
                if (c.equals(BigInteger.ONE.negate())) {
                    term += "-";
                } else if (!c.equals(BigInteger.ONE)) {
                    term += c + "*";
                }

                for (int i = 1; i < 4; i++) {
                    if (!indexs[i].equals(BigInteger.ZERO)) {
                        term +=  type[i];
                        if (!indexs[i].equals(BigInteger.ONE)) {
                            if (i == 1 && indexs[i].equals(new BigInteger("2"))) {
                                term += "*x";
                            } else { term += "**" + indexs[i]; }

                        }
                        term += "*";
                    }

                }

                term = term.substring(0, term.length() - 1);
            }

            // put the positive one at the beginning
            if (c.compareTo(BigInteger.ZERO) > 0) {
                if (first) {
                    outbuilder.insert(0, term);
                    first = false;
                } else {
                    term = "+" + term;
                    outbuilder.append(term);
                }
            } else {
                outbuilder.append(term);
            }
        }

        String output = outbuilder.toString();
        if (output.equals("")) { output = "0"; }
        return output;
    }

    public void simplify() {
        merge();
        mergeTri();
    }

    private void merge() {
        // AL to map
        HashMap<Triple, Term> map = new HashMap<>();
        for (Term t : terms) {
            Triple triple = t.getTriple();
            if (map.get(triple) == null) {
                map.put(triple, t);
            } else {
                map.get(triple).Merge(t);
            }
        }
        // map to AL
        ArrayList<Term> newArr = new ArrayList<>();
        for (Term t : map.values()) {
            newArr.add(t);
        }
        terms = newArr;
    }

    private void mergeTri() {
        //ArrayList to hashMap
        HashMap<BigInteger, ArrayList<Term>> map = new HashMap<>();
        for (Term t : terms) {
            // pow的index编号为1
            BigInteger powindex = t.getIndexs()[1];
            if (map.get(powindex) == null) {
                ArrayList<Term> terms = new ArrayList<>();
                terms.add(t);
                map.put(powindex, terms);
            } else {
                map.get(powindex).add(t);
            }
        }
        // merger tri
        //System.out.println(this.toString().length());
        for (int time = 0; time < 100; time++) {
            // merge tri one time
            for (Map.Entry<BigInteger, ArrayList<Term>> entry : map.entrySet()) {
                BigInteger powindex = entry.getKey();
                ArrayList<Term> terms = entry.getValue();
                Collections.shuffle(terms);
                ArrayList<Term> newterms = new ArrayList<>();
                for (int i = 0; i < terms.size(); i++) {
                    for (int j = 0; j < terms.size(); j++) {
                        if (i != j && terms.get(i) != null && terms.get(j) != null) {
                            Exp merged = mergeTwoTri(terms.get(i), terms.get(j), powindex);
                            if (merged != null) {
                                // add the new terms to new array
                                newterms.addAll(merged.terms);
                                terms.set(i, null);
                                terms.set(j, null);
                            }
                        }
                    }
                }
                for (int i = 0; i < terms.size(); i++) {
                    if (terms.get(i) != null) {
                        newterms.add(terms.get(i));
                    }
                }
                entry.setValue(newterms);
            }
            // map to array
            ArrayList<Term> newterms = new ArrayList<>();
            for (Map.Entry<BigInteger, ArrayList<Term>> entry : map.entrySet()) {
                for (Term t : entry.getValue()) {
                    newterms.add(t);
                }
            }
            terms = newterms;
            //System.out.println(this.toString().length());
        }
        merge();
        //System.out.println(this.toString().length());
    }

    private Exp mergeTwoTri(Term ta, Term tb, BigInteger powindex) {
        Term t1 = ta;
        Term t2 = tb;

        BigInteger m1 = t1.getIndexs()[2];
        BigInteger n1 = t1.getIndexs()[3];
        BigInteger m2 = t2.getIndexs()[2];
        BigInteger n2 = t2.getIndexs()[3];
        BigInteger two = new BigInteger("2");

        // recognize mode
        int mode = -1;
        boolean equals1 = n1.subtract(n2).abs().equals(two);
        if (m1.equals(m2) && equals1) {
            if (n1.subtract(n2).equals(two)) {
                Term tmp = t1;
                t1 = t2;
                t2 = tmp;
            }
            mode = 1;
        }
        boolean equals2 = m1.subtract(m2).abs().equals(two);
        if (equals2 && n1.equals(n2)) {
            if (m1.subtract(m2).equals(two)) {
                Term tmp = t1;
                t1 = t2;
                t2 = tmp;
            }
            mode = 2;
        }

        if (equals2 && equals1 &&
                m1.subtract(m2).multiply(n1.subtract(n2)).compareTo(BigInteger.ZERO) < 0) {
            if (m1.subtract(m2).equals(two)) {
                Term tmp = t1;
                t1 = t2;
                t2 = tmp;
            }
            mode = 3;
        }
        return implementMerge(powindex, t1, t2, mode);

    }

    private Exp implementMerge(BigInteger powindex, Term t1, Term t2, int mode) {
        BigInteger a = t1.getIndexs()[0];
        BigInteger m1 = t1.getIndexs()[2];
        BigInteger n1 = t1.getIndexs()[3];
        BigInteger b = t2.getIndexs()[0];
        BigInteger two = new BigInteger("2");
        //m2 = t2.getIndexs()[2];
        //n2 = t2.getIndexs()[3];
        // merge tri

        if (mode == -1) { return null; }
        Term t3;
        Term t4;
        Term t5;
        Term t6;
        if (mode == 1) {
            t3 = new Term(a.add(b), powindex, m1, n1);
            t4 = new Term(b.negate(), powindex, m1.add(two), n1);
            t5 = new Term(a.add(b), powindex, m1, n1.add(two));
            t6 = new Term(a, powindex, m1.add(two), n1);

        } else if (mode == 2) {
            t3 = new Term(a.add(b), powindex, m1, n1);
            t4 = new Term(b.negate(), powindex, m1, n1.add(two));
            t5 = new Term(a, powindex, m1, n1.add(two));
            t6 = new Term(a.add(b), powindex, m1.add(two), n1);
        } else  {
            t3 = new Term(a, powindex, m1, n1.subtract(two));
            t4 = new Term(b.subtract(a), powindex, m1.add(two), n1.subtract(two));
            t5 = new Term(b, powindex, m1, n1.subtract(two));
            t6 = new Term(a.subtract(b), powindex, m1, n1);
        }

        Exp exp1 = new Exp(t1);
        exp1.add(t2);
        int len1 = exp1.toString().length();

        Exp exp2 = new Exp(t3);
        exp2.add(t4);
        int len2 = exp2.toString().length();
        Exp exp3 = new Exp(t5);
        exp3.add(t6);
        int len3 = exp3.toString().length();

        // 当还是原来的形式最短时，认为没有进行合并
        // 避免进入死循环
        if (len1 < len2 && len1 < len3) { return null; }
        if (len2 < len3) { return exp2; }
        return exp3;
    }
}





