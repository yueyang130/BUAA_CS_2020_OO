package hwtwo;

import com.oocourse.spec2.main.Runner;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class,
                MyGroup.class);
        runner.run();
    }
}