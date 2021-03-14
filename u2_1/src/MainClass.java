import com.oocourse.TimableOutput;
import java.util.concurrent.LinkedBlockingQueue;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        TimableOutput.initStartTimestamp();

        LinkedBlockingQueue<Person> pout = new LinkedBlockingQueue<Person>();
        LinkedBlockingQueue<Person> pin = new LinkedBlockingQueue<Person>();
        LinkedBlockingQueue<Instruction> instrs = new LinkedBlockingQueue<Instruction>();

        Input myInput = new Input(pout);
        myInput.start();
        // let input thread run first
        Elevator elevator = new Elevator(pin, pout, instrs, myInput);
        Controller controller = new Controller(pin, pout, instrs, elevator, myInput);
        elevator.start();
        controller.start();
    }
}
