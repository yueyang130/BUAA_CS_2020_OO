import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private String exp;
    private String signedInt;
    private String index;
    private String pow;
    private String sin;
    private String cos;
    private String triF;
    private String otherfactor;
    private String expFactor;
    private String factor;
    private String term;

    public Parser() {
        signedInt = "[+-]?\\d+";
        index = "\\*\\*[ \t]*" + signedInt;
        pow = "x(?:[ \t]*" + index + ")?";
        // triF and exp factor use []
        triF = "(sin|cos)[ \t]*\\[[ \t]*(.*?)[ \t]*\\](?:[ \t]*" + index + ")?";
        expFactor = "\\[(.*?)\\]";
        otherfactor = "(?:" + signedInt + ")|(?:" + pow + ")";
        factor = "((" + expFactor + ")|" + otherfactor + "|(" + triF + "))";
        term = "(?:[+-][ \t]*)?" + factor +
                "(?:[ \t]*\\*[ \t]*" + factor + ")*";
        exp = "[ \t]*(?:[+-][ \t]*)?" + term + "[ \t]*" +
                "(?:[+-][ \t]*" + term + "[ \t]*)*";
    }

    public boolean ExpJudger(String in) {
        // ensure no '[' or ']' are included in exp
        for (int i = 0; i < in.length(); i++) {
            if (in.charAt(i) == '[' || in.charAt(i) == ']') {
                return false;
            }
        }
        return judgeExp(in);
    }

    public boolean judgeExp(String input) {
        String in = input;
        // replace the most external bracket
        StringBuilder builder = new StringBuilder(in);
        if (!replaceBracket(builder)) { return false; }
        in = builder.toString();

        // see if the whole string  match with exp, excluded exp factor
        Pattern p = Pattern.compile(exp);
        Matcher m = p.matcher(in);
        if (!m.find()) { return false; }
        if (!m.group(0).equals(in)) { return false; }

        // recursion to see if factor in TriF match with factor
        String factorInTri = "(sin|cos)[ \t]*\\[[ \t]*(?<factor>.*?)[ \t]*\\]";
        p = Pattern.compile(factorInTri);
        m = p.matcher(in);
        while (m.find()) {
            String factorFind = m.group("factor");
            if (!judgeTri(factorFind)) {
                return false;
            }
        }

        // recursion to see if exp factor out of TriF match with exp
        String expOutTri = "(?<!(sin|cos))[ \t]*\\[(?<exp>.*?)\\]";
        p = Pattern.compile(expOutTri);
        m = p.matcher(in);
        while (m.find()) {
            String expFind = m.group("exp");
            if (!judgeExp(expFind)) {
                return false;
            }
        }
        return true;
    }

    public boolean judgeTri(String input) {
        // to see if string  found in triF match with factor
        String in = input;

        // to see if factor match with  Const or Pow
        Pattern p = Pattern.compile(otherfactor);
        Matcher m = p.matcher(in);
        if (m.matches()) { return true; }

        // replace the most external bracket
        StringBuilder builder = new StringBuilder(in);
        if (!replaceBracket(builder)) { return false; }
        in = builder.toString();

        // to see if factor match with triF
        String factorInTri =
                "(sin|cos)[ \t]*\\[[ \t]*(?<factor>.*?)[ \t]*\\](?:[ \\t]*" + index + ")?";
        p = Pattern.compile(factorInTri);
        m = p.matcher(in);
        if (m.find()) {
            if (m.group(0).equals(in)) {
                String factorFind = m.group("factor");
                if (judgeTri(factorFind)) { return true; }
            }
        }

        // to see if factor match with exp factor
        if (in.startsWith("[") && in.endsWith("]")) {
            in = in.substring(1, in.length() - 1);
            if (judgeExp(in)) {
                return true;
            }
        }

        return false;
    }

    public Exp expParser(String input) throws IndexException {
        // delete sapce
        String in = input;

        in = in.replaceAll("[ \t]*","");
        // merge op and sign
        in = in.replaceAll("\\+-|-\\+", "-");
        in = in.replaceAll("\\+\\+|--", "+");
        in = in.replaceAll("\\+-|-\\+", "-");
        in = in.replaceAll("\\+\\+|--", "+");
        // add sign at the beginning if needed
        if (!in.startsWith("+") && !in.startsWith("-")) {
            in = "+" + in;
        }
        // parse
        // replace the most external bracket
        // 因为此处的TriF和expfactor都是[]
        StringBuilder builder = new StringBuilder(in);
        replaceBracket(builder);
        in = builder.toString();

        // 经过前面的处理， 每个term前面都有且只有一个符号
        String newTerm = "([+-])" +   factor +
                "(\\*" + factor + ")*";
        Pattern p = Pattern.compile(newTerm);
        Matcher m = p.matcher(in);
        Exp myExp = new Exp();

        while (m.find()) {
            Term t = termParser(m.group(0));
            myExp.add(t);
        }
        return myExp;
    }

    public Term termParser(String input) throws IndexException {
        String in = input;
        Term myTerm = new Term();

        if (in.startsWith("-")) {
            myTerm.add(FactorFactory.getFactor("const", null, new BigInteger("-1")));
        } else {
            myTerm.add(FactorFactory.getFactor("const", null, new BigInteger("1")));
        }
        in = in.substring(1);

        Pattern p = Pattern.compile(factor);
        Matcher m = p.matcher(in);
        while (m.find()) {
            String factorS = m.group(0);
            Factor f = factorParser(factorS);
            myTerm.add(f);
        }
        return myTerm;
    }

    public Factor factorParser(String input) throws IndexException {
        final String  newInt = "(?<coef>[+-]?\\d+)";
        final String newPow = "x(\\*\\*(?<index>[+-]?\\d+))?";
        final String newSin = "sin\\[(?<factor>.*?)\\](\\*\\*(?<index>[+-]?\\d+))?";
        final String newCos = "cos\\[(?<factor>.*?)\\](\\*\\*(?<index>[+-]?\\d+))?";
        final String newExp = "\\[(?<exp>.*?)\\]";

        Pattern[] p = new Pattern[5];
        Matcher[] m = new Matcher[5];
        p[0] = Pattern.compile(newInt);
        p[1] = Pattern.compile(newPow);
        p[2] = Pattern.compile(newSin);
        p[3] = Pattern.compile(newCos);
        p[4] = Pattern.compile(newExp);

        for (int i = 0; i < 5; i++) {
            m[i] = p[i].matcher(input);
        }

        BigInteger index;
        BigInteger limit = new BigInteger("50");
        String[] type = {"const", "pow", "sin", "cos", "exp"};

        if (m[0].matches()) {
            index = new BigInteger(m[0].group("coef"));
            return FactorFactory.getFactor(type[0], null, index);
        }
        for (int i = 1; i < 4; i++) {
            if (m[i].matches()) {
                String indexS = m[i].group("index");
                if (indexS == null) { indexS = "1"; }
                index = new BigInteger(indexS);
                if (index.abs().compareTo(limit) > 0) {
                    throw new IndexException();
                }
                Factor f;
                if (i == 2 || i == 3) {
                    String fs = m[i].group("factor");
                    StringBuilder fsbuilder = new StringBuilder(fs);
                    replaceBracket(fsbuilder);
                    fs = fsbuilder.toString();
                    f = factorParser(fs);
                } else { f = null; }
                return FactorFactory.getFactor(type[i], f, index);
            }
        }
        if (m[4].matches()) {
            String expS = m[4].group("exp");
            return new  ExpFactor(expParser(expS));
        }
        System.out.println("not match a correct type!");
        return null;
    }

    private boolean replaceBracket(StringBuilder builder) {
        int leftnum = 0;
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '(') {
                leftnum++;
                if (leftnum == 1) { builder.setCharAt(i, '['); }
            } else if (builder.charAt(i) == ')') {
                if (leftnum == 1) { builder.setCharAt(i, ']'); }
                leftnum--;
            }
        }
        if (leftnum != 0)  { return false; }
        return true;
    }
}