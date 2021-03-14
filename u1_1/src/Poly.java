import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class Poly  {
    private  TreeMap<BigInteger, BigInteger> poly = new TreeMap<>();

    public Poly(ArrayList<BigInteger> coef, ArrayList<BigInteger> index) {
        // 合并index相同的项
        for (int i = 0; i < coef.size(); i++) {
            // 若键不存在，放入coef_i,否则放入coef_i与原值之和
            poly.merge(index.get(i), coef.get(i), BigInteger::add);
        }
    }

    public TreeMap<BigInteger, BigInteger> getPoly() {
        return poly;
    }

    public Poly diff() {
        ArrayList<BigInteger> dcoef = new ArrayList<>();
        ArrayList<BigInteger> dindex = new ArrayList<>();
        poly.forEach((index, coef) -> {
            if (!index.equals(BigInteger.ZERO) && !coef.equals(BigInteger.ZERO)) {
                dcoef.add(coef.multiply(index));
                dindex.add(index.subtract(BigInteger.ONE));
            }
        });
        return new Poly(dcoef, dindex);
    }
}
