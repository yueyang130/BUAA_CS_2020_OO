package hwthree;

import com.oocourse.spec3.main.Person;

public class Path implements Comparable<Path> {
    private int dist;
    private Person preNode;
    private Person node;

    public Path(int dist, Person preNode, Person node) {
        this.dist = dist;
        this.preNode = preNode;
        this.node = node;
    }

    public int getDist() {
        return dist;
    }

    public Person getPreNode() {
        return preNode;
    }

    public Person getNode() {
        return node;
    }

    @Override
    public int compareTo(Path o) {
        return Integer.compare(dist, o.getDist());
    }
}
