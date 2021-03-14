import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MyNetwork implements Network {
    private ArrayList<Person> people;

    public MyNetwork() {
        people = new ArrayList<>();
    }

    @Override
    public boolean contains(int id) {
        for (Person p : people) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        //if (!contains(id)) { return null; }
        for (Person p : people) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person p : people) {
            if (p.equals(person)) {
                throw new EqualPersonIdException();
            }
        }
        people.add(person);
    }

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

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        int sum = 0;
        for (Person person : people) {
            if (compareName(id, person.getId()) > 0) {
                sum += 1;
            }
        }
        return sum + 1;

    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) { return true; }
        boolean[] visited = new boolean[people.size()];
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
    }

    public  static void main(String[] args) {
        List<Integer> people = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            people.add(i);
        }
        List<Integer> ls = new ArrayList<>();
        ls.add(0);
        for (int i = 0; i < people.size(); i++) {
            for (int j = 0; j < ls.size(); j++) {
                if (people.get(i) == ls.get(j) + 1) {
                    Integer s = people.remove(i);
                    ls.add(s);
                    System.out.println("people : " + people);
                    System.out.println("ls : " + ls);
                }
            }

        }
        System.out.println(ls);
    }
}
