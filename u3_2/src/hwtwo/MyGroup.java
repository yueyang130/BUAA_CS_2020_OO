package hwtwo;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MyGroup implements Group {
    private int groupCapacity = 2048;
    private int id;
    private Map<Integer, Person> people;
    // 以下是结果的中间变量
    private int sumAge;
    private int sumAge2;
    private BigInteger conflictSum;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>(groupCapacity);
        this.sumAge = 0;
        this.sumAge2 = 0;
        this.conflictSum = BigInteger.ZERO;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Group)) {
            return false;
        }
        return ((Group) obj).getId() == id;
    }

    @Override
    public void addPerson(Person person) {
        people.put(person.getId(), person);
        sumAge += person.getAge();
        sumAge2 += person.getAge() * person.getAge();
        conflictSum = conflictSum.xor(person.getCharacter());
    }

    @Override
    public boolean hasPerson(Person person) {
        // 这里为了追求性能没有使用规格里面的equals方法
        // 而是直接使用了hash来判断
        if (person == null) {
            return false;
        }
        //if (!(person instanceof Person)) {
        //    return false;
        //}
        return people.containsKey(person.getId());
    }

    @Override
    public int getRelationSum() {
        int sum = 0;
        // 双方都需要保证在group中
        for (Person p1 : people.values()) {
            for (Person p2 : people.values()) {
                if (p1.isLinked(p2)) {
                    sum += 1;
                }
            }
        }
        return sum;
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        // 双方都需要保证在group中
        for (Person p1 : people.values()) {
            for (Person p2 : people.values()) {
                if (p1.isLinked(p2)) {
                    sum += p1.queryValue(p2);
                }
            }
        }
        return sum;
    }

    @Override
    public BigInteger getConflictSum() {
        if (people.size() == 0) {
            return BigInteger.ZERO;
        }
        //Iterator<Person> iter = people.values().iterator();
        //BigInteger result = iter.next().getCharacter();
        //while (iter.hasNext()) {
        //    result = result.xor(iter.next().getCharacter());
        //}
        return conflictSum;
    }

    /**
     * 由采用遍历的方式改为采用保存为sum为静态域的方式
     * 复杂度由O(n)下降为O（1）
     * @return mean of age
     */
    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        //int sum = 0;
        //for (Person p : people.values()) {
        //    sum += p.getAge();
        //}
        return sumAge / people.size();
    }

    /**
     * 考虑到涉及mean的精确度问题，并不采用保存sumAge2的方法
     * 采用直接遍历，复杂度为O(n)
     * @return derivation
     */
    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int mean = getAgeMean();
        int n = people.size();
        //int sum = 0;
        //for (Person p : people.values()) {
        //    sum += (p.getAge() - mean) * (p.getAge() - mean);
        //}
        //return sum / people.size();
        //return (sumAge2 - sumAge * sumAge / people.size()) / people.size();
        return (sumAge2 - 2 * sumAge * mean + n * mean * mean) / n;
    }

    public int getPeopleSum() {
        return people.size();
    }

}
