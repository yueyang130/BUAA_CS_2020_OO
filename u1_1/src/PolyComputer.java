import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolyComputer {
    private Poly poly;

    public void parsePoly(String s) {
        // 原本字符串的转义字符只用一个'\'
        // 而正则表达式中的\\表示， 而表示一个普通的反斜杠是 \\\\。
        String reg = "[ \t]*(?<op>[+|-]?)[ \t]*(?<coef>[+-]?\\d*)"
                + "[ \t]*(\\*)?[ \t]*"
                + "(?<x>x)?[ \t]*"
                + "(((\\*\\*)[ \t]*(?<index1>\\d+))|((\\*\\*)[ \t]*(?<index2>[+-]\\d+)))?[ \t]*";


        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(s);
        /*
        while (m.find()) {
            System.out.println(m.group("op"));
            System.out.println(m.group("coef"));
            System.out.println(m.group("x"));
            System.out.println(m.group("index"));
        }
        */
        ArrayList<BigInteger> coef = new ArrayList<>();
        ArrayList<BigInteger> index = new ArrayList<>();
        while (m.find()) {
            String coefS = m.group("coef");
            String index1S = m.group("index1");
            String index2S = m.group("index2");
            String opS = m.group("op");
            String xs = m.group("x");

            BigInteger c;
            //
            if (!m.group(0).equals("")) {
                if (coefS.equals("") || coefS.equals("+")) {
                    c = BigInteger.ONE;
                } else if (coefS.equals("-")) {
                    c = BigInteger.ONE.negate();
                } else {
                    c = new BigInteger(coefS);
                }
                if (opS.equals("-")) {
                    c = c.negate();
                }

                // 如果匹配到index为空串:匹配到x,index=1;没匹配到x,index=0
                BigInteger i;
                if (index1S == null && index2S == null) {
                    if (xs == null) {
                        i = BigInteger.ZERO;
                    } else {
                        i = BigInteger.ONE;
                    }
                } else if (index1S != null) {
                    i = new BigInteger(index1S);
                } else if (index2S != null) {
                    i = new BigInteger(index2S);
                } else {
                    i = BigInteger.valueOf(-99999);
                }
                //System.out.print("coeff is " + c);
                //System.out.println("  index is " + i);
                coef.add(c);
                index.add(i);
            }
        }
        poly = new Poly(coef, index);
    }

    public void printdPoly()  {
        // print output
        Poly dploy = poly.diff();
        StringBuilder outbuilder = new StringBuilder();
        //ArrayList<String> terms = new ArrayList<>();
        //  是否是第一个系数大于0的项
        boolean first = true;
        for (Map.Entry<BigInteger, BigInteger> e : dploy.getPoly().entrySet()) {
            BigInteger di = e.getKey();
            BigInteger dc = e.getValue();
            String term = "";
            if (di.equals(BigInteger.ZERO)) {
                term += dc;
            } else {
                if (dc.equals(BigInteger.ONE)) {
                    term += "x";
                } else if (dc.equals(BigInteger.ONE.negate())) {
                    term += "-x";
                } else { term += dc + "*x"; }

                if (!di.equals(BigInteger.ONE)) {
                    term += "**" + di;
                }
            }

            if (dc.compareTo(BigInteger.ZERO) > 0) {
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
        System.out.println(output);
    }

}
