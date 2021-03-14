import com.oocourse.TimableOutput;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class MainClass {
    public static void main(String[] args)  {
        // put initStamp at the head of the code
        TimableOutput.initStartTimestamp();

        LinkedBlockingQueue<Person> pout = new LinkedBlockingQueue<>();
        Input myInput = new Input(pout);
        myInput.start();
        while (myInput.getElevatorNum() == 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int nelevator = myInput.getElevatorNum();

        ArrayList<Elevator> arrElevator = new ArrayList<>();
        for (int i = 0; i < nelevator; i++) {
            Elevator elevator = new Elevator(i,I2F(i));
            arrElevator.add(elevator);
        }

        Controller controller = new Controller(pout, arrElevator, myInput);
        myInput.setController(controller);
        for (int i = 0; i < nelevator; i++) {
            arrElevator.get(i).setController(controller);
        }
        // start
        controller.start();
        for (int i = 0; i < nelevator; i++) {
            arrElevator.get(i).start();
        }

    }

    public static int I2F(int i) {
        // transform id to initial floor
        // 1 10 -3 16 6
        assert i >= 0 && i <= 4;
        if (i == 0) { return 1; }
        if (i == 1) { return 10; }
        if (i == 2) { return -3; }
        if (i == 3) { return 16; }
        return 6;
    }
}
