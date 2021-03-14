import com.oocourse.spec1.main.Person;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyPerson implements Person {
    private int id;
    private String name;
    private BigInteger character;
    private int age;
    private List<Person> acquaintance;
    private List<Integer> value;

    public MyPerson(int id, String name,
                    BigInteger character, int age) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.age = age;
        acquaintance = new ArrayList<>();
        value = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigInteger getCharacter() {
        return character;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (!(obj instanceof Person)) { return false; }
        return ((Person) obj).getId() == id;
    }

    @Override
    public boolean isLinked(Person person) {
        // note this.id == person.id also is thought as isLinked
        for (Person p : acquaintance) {
            if (p.getId() == person.getId()) {
                return true;
            }
        }
        if (id == person.getId()) {
            return true;
        }
        return false;
    }

    public void addAcquaintance(Person p, Integer v) {
        acquaintance.add(p);
        value.add(v);
    }

    @Override
    public int queryValue(Person person) {
        for (int i = 0; i < acquaintance.size();i++) {
            if (acquaintance.get(i).getId() == person.getId()) {
                return value.get(i);
            }
        }
        return 0;
    }

    @Override
    public int getAcquaintanceSum() {
        return acquaintance.size();
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] age = new int[100];
        int length = 0;
        int sumAge = 0;
        int sumAge2 = 0;
        while (in.hasNext()) {
            age[length] = in.nextInt();
            sumAge += age[length];
            sumAge2 += age[length] * age[length];
            length++;
        }
        int mean = sumAge / length;
        int temp = 0;
        for (int i = 0; i < length; i++) {
            temp += (age[i] - mean) * (age[i] - mean);
        }
        double d1 = (sumAge2 - 2 * mean * sumAge + length * mean * mean);
        d1 = 1.0 * d1 / length;
        double d2 = (sumAge2 * length - sumAge * sumAge);
        d2 = 1.0 * d2 / (length * length);
        double d3 = 1.0 * temp / length;
        System.out.println("advanced var formula : d1 = " + d1);
        System.out.println("var formula : d2 = " + d2);
        System.out.println("AML : d3 = " + d3);
//        assert d1 == d3;
//        assert d2 == d3;
    }
}
