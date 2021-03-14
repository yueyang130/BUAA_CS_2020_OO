package hwthree;

import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class MyNetwork implements Network {
    //private ArrayList<Person> people;
    private int peopleCapacity = 1100;
    private int groupsCapacity = 16;
    private Map<Integer, Person> people;
    private Map<Integer, Integer> money;
    private Map<Integer, Group> groups;
    private Map<Integer, Integer> group2relation;
    private Map<Integer, Integer> group2value;

    public MyNetwork() {
        people = new HashMap<>(peopleCapacity);
        money = new HashMap<>(peopleCapacity);
        groups = new HashMap<>(groupsCapacity);
        group2relation = new HashMap<>(groupsCapacity);
        group2value = new HashMap<>(groupsCapacity);
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
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
        money.put(person.getId(), 0);
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
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        return Graph.iscircle(peopleCapacity,
                p1, p2, people);
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
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new GroupIdNotFoundException();
        }
        if (!people.containsKey(id1)) {
            throw new PersonIdNotFoundException();
        }
        Group g = groups.get(id2);
        Person p = people.get(id1);
        if (!g.hasPerson(p)) {
            throw new EqualPersonIdException();
        }
        g.delPerson(p);
        // first addRelation, then delFromGroup
        int relationDecrement = 1;
        int valueDecrement = 0;
        Iterator<Person> iter = ((MyPerson) p).getAcquaintanceIter();
        try {
            while (iter.hasNext()) {
                Person acquian = iter.next();
                if (g.hasPerson(acquian)) {
                    relationDecrement += 2;
                    valueDecrement += 2 * queryValue(id1, acquian.getId());
                }
            }
        } catch (RelationNotFoundException e) {
            System.out.println("FUCKING! There are some errors in deleteFromGroup!");
        }
        group2relation.put(id2, group2relation.get(id2) - relationDecrement);
        group2value.put(id2, group2value.get(id2) - valueDecrement);
        // first delfromGroup, then add Relation
        ((MyPerson) p).delGroup(g.getId());
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

    @Override
    public int queryAgeSum(int l, int r) {
        int countAge = 0;
        for (Person p : people.values()) {
            if (p.getAge() >= l && p.getAge() <= r) {
                countAge += 1;
            }
        }
        return countAge;
    }

    @Override
    public int queryMinPath(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) {
            return 0;
        }
        if (!isCircle(id1, id2)) {
            return -1;
        }

        int personCount = 0;
        //Path[] paths = new Path[people.size()];
        Map<Integer, Path> paths = new HashMap<>(people.size() * 2);
        PriorityQueue<Path> cands = new PriorityQueue<>(4096);
        cands.add(new Path(0, getPerson(id1), getPerson(id1)));

        while (personCount < people.size() && !cands.isEmpty()) {
            Path path = cands.poll();
            int dist = path.getDist();
            //Person preNode = path.getPreNode();
            Person vmin = path.getNode();
            if (paths.get(vmin.getId()) != null) {
                continue;
            }
            paths.put(vmin.getId(), path);
            // 查看是否已经找到id2的最短路径
            if (vmin.getId() == id2) {
                break;
            }

            Iterator<Person> iter = ((MyPerson) vmin).getAcquaintanceIter();
            while (iter.hasNext()) {
                Person nextNode = iter.next();
                Path path1 = paths.get(nextNode.getId());
                int value = vmin.queryValue(nextNode);
                // 还没有找到最短路径的点，也就是不在集合paths里面的点
                // 当新距离小于当前距离才更新，但是无法找到之前的结点
                if (path1 == null) {
                    Path newPath = new Path(dist + value, vmin, nextNode);
                    cands.add(newPath);
                }
            }
            personCount += 1;
        }
        return paths.get(id2).getDist();

    }

    @Override
    public boolean queryStrongLinked(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) { return true; }
        // 特判，iscircle(id1,id2)
        if (!isCircle(id1, id2)) {
            return false;
        }
        // 特判。id1和id2直接相连
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        if (p1.isLinked(p2)) {
            return  Graph.iscircleUndirect(peopleCapacity, p1, p2, people);
        }
        // 枚举，看看是否存在割点
        boolean circle = true;
        Set<Integer> linkedNodes = Graph.getLinkedNode(peopleCapacity, p1, people);
        linkedNodes.remove(id1);
        linkedNodes.remove(id2);
        for (Integer maskid : linkedNodes) {
            if (!Graph.iscircle(peopleCapacity, p1, p2, people, maskid)) {
                circle = false;
                break;
            }
        }
        return circle;
    }

    /**
     * 懒得写并查集了，直接BFS。对每个连通子图Gs(Vs, Es)，时间复杂度都是O(Vs+Es)
     * 总的时间复杂度就是O(V+E)
     * @return 连通子图个数
     */
    @Override
    public int queryBlockSum() {
        return Graph.queryBlockSum(peopleCapacity, people);
    }

    @Override
    public void borrowFrom(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualPersonIdException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) {
            throw new EqualPersonIdException();
        }
        money.put(id1, money.get(id1) - value);
        money.put(id2, money.get(id2) + value);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        return money.get(id);
    }
}
