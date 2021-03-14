package hwthree;

import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    public static boolean iscircle(int peopleCapacity,
                   Person p1, Person p2, Map<Integer, Person> people) {
        List<Person> queue = new ArrayList<>(peopleCapacity);
        Map<Integer, Boolean> visited = new HashMap<>(peopleCapacity);
        for (Integer id : people.keySet()) {
            visited.put(id, false);
        }

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
    }

    public static boolean iscircle(int peopleCapacity,
                                   Person p1, Person p2, Map<Integer, Person> people, int maskId) {
        List<Person> queue = new ArrayList<>(peopleCapacity);
        Map<Integer, Boolean> visited = new HashMap<>(peopleCapacity);
        for (Integer id : people.keySet()) {
            visited.put(id, false);
        }

        queue.add(p1);
        visited.put(p1.getId(), true);

        while (!queue.isEmpty()) {
            MyPerson curr = (MyPerson) queue.remove(0);
            Iterator<Person> iter = curr.getAcquaintanceIter();
            while (iter.hasNext()) {
                Person p = iter.next();
                if (p.getId() != maskId) {
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

        }
        return false;
    }

    public static boolean iscircleUndirect(int peopleCapacity,
                                   Person p1, Person p2, Map<Integer, Person> people) {
        List<Person> queue = new ArrayList<>(peopleCapacity);
        Map<Integer, Boolean> visited = new HashMap<>(peopleCapacity);
        for (Integer id : people.keySet()) {
            visited.put(id, false);
        }

        queue.add(p1);
        visited.put(p1.getId(), true);

        while (!queue.isEmpty()) {
            MyPerson curr = (MyPerson) queue.remove(0);
            Iterator<Person> iter = curr.getAcquaintanceIter();
            while (iter.hasNext()) {
                Person p = iter.next();
                if (!curr.equals(p1) || !p.equals(p2)) {
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

        }
        return false;
    }

    public static Set<Integer> getLinkedNode(int peopleCapacity,
                                   Person p1, Map<Integer, Person> people) {
        List<Person> queue = new ArrayList<>(peopleCapacity);
        Map<Integer, Boolean> visited = new HashMap<>(peopleCapacity);
        for (Integer id : people.keySet()) {
            visited.put(id, false);
        }

        queue.add(p1);
        visited.put(p1.getId(), true);
        Set<Integer> linkedNode = new HashSet<>(peopleCapacity);

        while (!queue.isEmpty()) {
            MyPerson curr = (MyPerson) queue.remove(0);
            Iterator<Person> iter = curr.getAcquaintanceIter();
            while (iter.hasNext()) {
                Person p = iter.next();
                boolean vis = visited.get(p.getId());
                if (!vis) {
                    queue.add(p);
                    visited.put(p.getId(), true);
                    linkedNode.add(p.getId());
                }
            }

        }
        return linkedNode;
    }

    public static int queryBlockSum(int peopleCapacity, Map<Integer, Person> people) {
        int blockCount = 0;

        if (people.isEmpty()) {
            return blockCount;
        }

        List<Person> queue = new ArrayList<>(peopleCapacity);
        Set<Integer> unvisited = new HashSet<>(peopleCapacity);

        unvisited.addAll(people.keySet());
        Person p1 = null;
        for (Person p : people.values()) {
            p1 = p;
            break;
        }

        queue.add(p1);
        unvisited.remove(p1.getId());
        blockCount++;

        while (true) {
            while (!queue.isEmpty()) {
                MyPerson curr = (MyPerson) queue.remove(0);
                Iterator<Person> iter = curr.getAcquaintanceIter();
                while (iter.hasNext()) {
                    Person p = iter.next();
                    if (unvisited.contains(p.getId())) {
                        queue.add(p);
                        unvisited.remove(p.getId());
                    }
                }
            }
            if (unvisited.isEmpty()) { break; }
            for (Integer id : unvisited) {
                p1 = people.get(id);
                break;
            }
            queue.add(p1);
            unvisited.remove(p1.getId());
            blockCount++;
        }

        return blockCount;
    }
}
