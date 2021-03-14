import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Input extends Thread {
    private LinkedBlockingQueue<Person> pout;

    public Input(LinkedBlockingQueue<Person> pout) {
        this.pout = pout;
    }

    public void run()  {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            } else {
                Person p = new Person(request.getPersonId(),
                        request.getFromFloor(), request.getToFloor());
                pout.add(p);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            System.out.println("encounter IOException in Input!");
        }
    }
}
