import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Input extends Thread {
    private final LinkedBlockingQueue<Person> pout;
    private int elevatorNum = 0;
    private Controller controller;

    public Input(LinkedBlockingQueue<Person> pout) {
        this.pout = pout;
    }

    public void run()  {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        elevatorNum = elevatorInput.getElevatorNum();
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            } else {
                Person p = new Person(request.getPersonId(),
                        request.getFromFloor(), request.getToFloor());
                //TimableOutput.println("we have input " + p.getId());
                synchronized (pout) {
                    pout.add(p);
                    //TimableOutput.println(p.getId() + " enter pout queue");
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            System.out.println("encounter IOException in Input!");
        }
    }

    public int getElevatorNum() {
        return elevatorNum;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
