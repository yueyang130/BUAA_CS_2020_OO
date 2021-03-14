public class Person {
    private int id;
    private int start;
    private int target;

    Person(int id, int start, int target) {
        this.id = id;
        this.start = start;
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getTarget() {
        return target;
    }
}
