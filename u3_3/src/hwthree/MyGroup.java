package hwthree;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MyGroup implements Group {
    private int groupCapacity = 1024;
    private int id;
    private Map<Integer, Person> people;
    // �����ǽ�����м����
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
    public void delPerson(Person person) {
        people.remove(person.getId());
        sumAge -= person.getAge();
        sumAge2 -= person.getAge() * person.getAge();
        conflictSum = conflictSum.xor(person.getCharacter());
    }

    @Override
    public boolean hasPerson(Person person) {
        // ����Ϊ��׷������û��ʹ�ù�������equals����
        // ����ֱ��ʹ����hash���ж�
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
        // ˫������Ҫ��֤��group��
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
        // ˫������Ҫ��֤��group��
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
     * �ɲ��ñ����ķ�ʽ��Ϊ���ñ���ΪsumΪ��̬��ķ�ʽ
     * ���Ӷ���O(n)�½�ΪO��1��
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
     * ���ǵ��漰mean�ľ�ȷ�����⣬�������ñ���sumAge2�ķ���
     * ����ֱ�ӱ��������Ӷ�ΪO(n)
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
