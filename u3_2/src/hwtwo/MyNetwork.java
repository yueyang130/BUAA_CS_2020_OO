package hwtwo;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    //private ArrayList<Person> people;
    private int peopleCapacity = 8192;
    private int groupsCapacity = 16;
    private Map<Integer, Person> people;
    private Map<Integer, Group> groups;
    private Map<Integer, Integer> group2relation;
    private Map<Integer, Integer> group2value;

    public MyNetwork() {
        people = new HashMap<>(peopleCapacity);
        groups = new HashMap<>(groupsCapacity);
        group2relation = new HashMap<>(groupsCapacity);
        group2value = new HashMap<>(groupsCapacity);
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
        /*
        for (Person p : people) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;

         */
    }

    @Override
    public Person getPerson(int id) {
        //if (!contains(id)) { return null; }
        /*
        for (Person p : people) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
        */
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (person == null) {
            return;
        }
        if (people.containsKey(person.getId())) {
            throw new EqualPersonIdException();
        }
        people.put(person.getId(), person);
        /*
        for (Person p : people) {
            if (p.equals(person)) {
                throw new EqualPersonIdException();
            }
        }
        people.add(person);
         */
    }

    /**
     * 同时修改relationSum & valueSem, 需要求id1和id2的group的并集
     * 采用红黑树
     * 时间复杂度O(len(id1.groups.size) + len(id2.groups.size))
     * 此处考虑groups <= 10, 使用treeset可能难以有显著效果，因此使用了hashset
     * @param id1 id1
     * @param id2 id2
     * @param value value
     * @throws PersonIdNotFoundException ..
     * @throws EqualRelationException ..
     */
    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (id1 == id2 && contains(id1)) { return; }
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new EqualRelationException();
        }
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        ((MyPerson) p1).addAcquaintance(p2, value);
        ((MyPerson) p2).addAcquaintance(p1, value);

        // first addtoGroup, then addRelation
        for (Integer g1 : ((MyPerson) p1).getGroup()) {
            for (Integer g2 : ((MyPerson) p2).getGroup()) {
                if (g1.equals(g2)) {
                    group2relation.put(g1, group2relation.get(g1) + 2);
                    group2value.put(g1, group2value.get(g1) + 2 * value);
                }
            }
        }

    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new RelationNotFoundException();
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public BigInteger queryConflict(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id1).getCharacter().xor(getPerson(id2).getCharacter());
    }

    @Override
    public int queryAcquaintanceSum(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id).getAcquaintanceSum();
    }

    @Override
    public int compareAge(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id1).getAge() - getPerson(id2).getAge();
    }

    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id1).getName().compareTo(getPerson(id2).getName());
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    /**
     * 目前的复杂度基本是O(n)，从id->Person虽然理论上是O(1)但是仍然有性能损失
     * 为了提高效率，直接使用Person遍历，不采用id->person
     * @param id id
     * @return rank
     * @throws PersonIdNotFoundException ..
     */
    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        int sum = 0;
        Person p1 = getPerson(id);
        for (Person p2 : people.values()) {
            if (p1.getName().compareTo(p2.getName()) > 0) {
                sum += 1;
            }
        }
        return sum + 1;
        /*
        for (Person person : people) {
            if (compareName(id, person.getId()) > 0) {
                sum += 1;
            }
        }
        return sum + 1;
        */
    }

    /**
     * 由类似于邻接矩阵的形式改为邻接链表的形式
     * 算法复杂度由O(V2)下降为O(V+E)
     * @param id1 id1
     * @param id2 id2
     * @return boolean isCircle
     * @throws PersonIdNotFoundException ...
     */
    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) { return true; }

        List<Person> queue = new ArrayList<>(peopleCapacity);
        Map<Integer, Boolean> visited = new HashMap<>(peopleCapacity);
        for (Integer id : people.keySet()) {
            visited.put(id, false);
        }
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        queue.add(p1);
        visited.put(p1.getId(), true);

        while (!queue.isEmpty()) {
            MyPerson curr = (MyPerson) queue.remove(0);
            Iterator<Person> iter = curr.getAcquaintanceIter();
            while (iter.hasNext()) {
                Person p = iter.next();
                boolean vis = visited.get(p.getId());
                if (!vis) {
                    if (p.equals(p2)) {
                        return true;
                    }
                    queue.add(p);
                    visited.put(p.getId(), true);
                }
            }

        }
        return false;

        //boolean[] visited = new boolean[people.size()];
        /*
        // index ArrayList
        // 因此获取人的时候不是getPerson(id)而是people.get(index)
        List<Integer> queue = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getId() == id1) {
                queue.add(i);
                visited[i] = true;
                break;
            }
        }

        while (!queue.isEmpty()) {
            Person curr = people.get(queue.remove(0));
            for (int i = 0; i < people.size(); i++) {
                if (!visited[i] && curr.isLinked(people.get(i))) {
                    queue.add(i);
                    visited[i] = true;
                    if (people.get(i).getId() == id2) {
                        return true;
                    }
                }
            }
        }
        return false;

        */
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (group == null) {
            return;
        }
        if (groups.containsKey(group.getId())) {
            throw new EqualGroupIdException();
        }
        groups.put(group.getId(), group);
        group2value.put(group.getId(), 0);
        group2relation.put(group.getId(), 0);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addtoGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new GroupIdNotFoundException();
        }
        if (!people.containsKey(id1)) {
            throw new PersonIdNotFoundException();
        }
        Group g = groups.get(id2);
        Person p = people.get(id1);
        if (g.hasPerson(p)) {
            throw new EqualPersonIdException();
        }
        if (((MyGroup) g).getPeopleSum() >= 1111) {
            return;
        }

        // first addRelation, then addtoGroup
        g.addPerson(p);
        int relationIncrement = 1;
        int valueIncrement = 0;
        Iterator<Person> iter = ((MyPerson) p).getAcquaintanceIter();
        try {
            while (iter.hasNext()) {
                Person acquain =  iter.next();
                if (g.hasPerson(acquain)) {
                    relationIncrement += 2;
                    valueIncrement += 2 * queryValue(id1, acquain.getId());
                }
            }
        } catch (RelationNotFoundException e) {
            System.out.println("FUCKING! There are some errors in addtoGroup!");
            System.exit(1);
        }
        group2relation.put(id2, group2relation.get(id2) + relationIncrement);
        group2value.put(id2, group2value.get(id2) + valueIncrement);

        // first addtoGroup, then add Relation
        ((MyPerson) p).addGroup(g.getId());

    }

    @Override
    public int queryGroupSum() {
        return groups.size();
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new GroupIdNotFoundException();
        }
        return ((MyGroup) groups.get(id)).getPeopleSum();
    }

    @Override
    public int queryGroupRelationSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new GroupIdNotFoundException();
        }
        //return groups.get(id).getRelationSum();
        return group2relation.get(id);
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new GroupIdNotFoundException();
        }
        //return groups.get(id).getValueSum();
        return group2value.get(id);
    }

    @Override
    public BigInteger queryGroupConflictSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new GroupIdNotFoundException();
        }
        return groups.get(id).getConflictSum();
    }

    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new GroupIdNotFoundException();
        }
        return groups.get(id).getAgeMean();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new GroupIdNotFoundException();
        }
        return groups.get(id).getAgeVar();
    }
}
