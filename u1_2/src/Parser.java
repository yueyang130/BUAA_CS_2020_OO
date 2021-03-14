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
    private String factor;
    private String term;

    public Parser() {
        signedInt = "[+-]?\\d+";
        index = "\\*\\*[ \t]*" + signedInt;
        pow = "x([ \t]*" + index + ")?";
        sin = "sin[ \t]*\\([ \t]*x[ \t]*\\)([ \t]*" + index + ")?";
        cos = "cos[ \t]*\\([ \t]*x[ \t]*\\)([ \t]*" + index + ")?";
        triF = sin + "|" + cos;
        factor = "((" + signedInt + ")|(" + pow + ")|(" + triF + "))";
        term = "([+-][ \t]*)?" + factor +
                "([ \t]*\\*[ \t]*" + factor + ")*";
        exp = "[ \t]*([+-][ \t]*)?" + term + "[ \t]*" +
                "([+-][ \t]*" + term + "[ \t]*)*";
    }

    public boolean expJudger(String in) {
        Pattern p = Pattern.compile(exp);
        Matcher m = p.matcher(in);
        if (!m.matches()) { return false; }
        return true;
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
            myTerm.add(FactorFactory.getFactor("const", new BigInteger("-1")));
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
        final String  newInt = "(?<index>[+-]?\\d+)";
        final String newPow =  "x(\\*\\*(?<index>[+-]?\\d+))?";
        final String newSin =  "sin\\(x\\)(\\*\\*(?<index>[+-]?\\d+))?";
        final String newCos =  "cos\\(x\\)(\\*\\*(?<index>[+-]?\\d+))?";

        Pattern[] p = new Pattern[4];
        Matcher[] m = new Matcher[4];
        p[0] = Pattern.compile(newInt);
        p[1] = Pattern.compile(newPow);
        p[2] = Pattern.compile(newSin);
        p[3] = Pattern.compile(newCos);

        for (int i = 0; i < 4; i++) {
            m[i] = p[i].matcher(input);
        }

        BigInteger index;
        BigInteger limit = new BigInteger("10000");
        String[] type = {"const", "pow", "sin", "cos"};

        if (m[0].matches()) {
            index = new BigInteger(m[0].group("index"));
            return FactorFactory.getFactor(type[0], index);
        }

        for (int i = 1; i < 4; i++) {
            if (m[i].matches()) {
                String indexS = m[i].group("index");
                if (indexS == null) { indexS = "1"; }
                index = new BigInteger(indexS);
                if (i == 1 && index.abs().compareTo(limit) > 0) {
                    throw new IndexException();
                }
                return FactorFactory.getFactor(type[i], index);
            }
        }
        System.out.println("not match a correct type!");
        return null;
    }

    /*
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            Parser myParser = new Parser();
            String line = in.nextLine();
            boolean tf = myParser.expJudger(line);  // true format
            if (tf) {
                try {
                    Exp myExp = myParser.expParser(line);
                    //Exp dexp = myExp.diff();
                    //dexp.simplify();
                    System.out.println(dexp.toString());
                    myExp.simplify();
                    System.out.println(myExp.toString());
                } catch (IndexException e) {
                    System.out.println("WRONG FORMAT!");
                }
            }
            else { System.out.println("WRONG FORMAT!"); }
        }
    }
     */
}