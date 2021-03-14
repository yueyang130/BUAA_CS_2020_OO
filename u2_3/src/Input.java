import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;
import java.io.IOException;

public class Input extends Thread {
    private final Controller controller;

    public Input(Controller controller) {
        this.controller = controller;
    }

    public void run()  {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                //TimableOutput.println("we have input " + p.getId());
                synchronized (controller) {
                    controller.put(request);
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

}
