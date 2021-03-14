package hwtwo;

import com.oocourse.spec2.main.Person;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyPerson implements Person {
    // 每个Person都占用一个初始容量很大的acquaintanceMap的话，可能会爆栈
    private int acquaintanceCapacity = 16;
    //private int groupsCapacity = 16;
    private int id;
    private String name;
    private BigInteger character;
    private int age;
    //private List<Person> acquaintance;
    //private List<Integer> value;
    private Map<Integer, Person> acquaintanceMap;
    private Map<Integer, Integer> value;
    private Set<Integer> groups;

    public MyPerson(int id, String name,
                    BigInteger character, int age) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.age = age;
        //acquaintance = new ArrayList<>();
        //value = new ArrayList<>();
        acquaintanceMap = new HashMap<>(acquaintanceCapacity);
        value = new HashMap<>(acquaintanceCapacity);
        groups = new HashSet<>();
    }

    public void addGroup(Integer id) {
        groups.add(id);
    }

    public Set<Integer> getGroup() {
        return groups;
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
        //for (Person p : acquaintance) {
        //    if (p.getId() == person.getId()) {
        //       return true;
        //    }
        //}
        if (acquaintanceMap.containsKey(person.getId())) {
            return true;
        }
        if (id == person.getId()) {
            return true;
        }
        return false;
    }

    public void addAcquaintance(Person p, Integer v) {
        acquaintanceMap.put(p.getId(), p);
        value.put(p.getId(), v);
    }

    @Override
    public int queryValue(Person person) {
        //for (int i = 0; i < acquaintance.size();i++) {
        //    if (acquaintance.get(i).getId() == person.getId()) {
        //        return value.get(i);
        //    }
        //}
        Integer v = value.get(person.getId());
        if (v != null) {
            return v;
        }
        return 0;
    }

    @Override
    public int getAcquaintanceSum() {
        return acquaintanceMap.size();
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public int hashCode() {
        return (new Integer(id)).hashCode();
    }

    public Iterator<Person> getAcquaintanceIter() {
        return acquaintanceMap.values().iterator();
    }
}
