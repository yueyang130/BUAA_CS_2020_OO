import java.math.BigInteger;
import java.util.Objects;

public class Triple {
    private BigInteger first;
    private BigInteger second;
    private BigInteger third;

    public Triple(BigInteger first, BigInteger second, BigInteger third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj.getClass() != getClass()) { return  false; }
        Triple o = (Triple) obj;
        return first.equals(o.first) &&
                second.equals(o.second) && third.equals(o.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
