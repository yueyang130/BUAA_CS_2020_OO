import java.math.BigInteger;

public class FactorFactory  {
    public static Factor getFactor(String type, BigInteger index) {
        if (type.equals("pow")) {
            return new Pow(index);
        } else if (type.equals("sin")) {
            return new Sin(index);
        } else if (type.equals("cos")) {
            return new Cos(index);
        } else if (type.equals("const")) {
            return new Const(index); // represent value
        }
        else {
            System.out.println("input type is not pow, sin or cos!");
            return null;
        }
    }
}
