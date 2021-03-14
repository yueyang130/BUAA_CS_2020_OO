import com.oocourse.TimableOutput;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator extends Thread {
    private int floor = 1;
    private ElevatorState state = ElevatorState.WAIT;
    private LinkedBlockingQueue<Person> pin;
    private LinkedBlockingQueue<Person> pout;
    private LinkedBlockingQueue<Instruction> instrs;
    private Input myInput;

    public Elevator(LinkedBlockingQueue<Person> pin,
                    LinkedBlockingQueue<Person> pout,
                    LinkedBlockingQueue<Instruction> instrs,
                    Input myInput) {
        this.pin = pin;
        this.pout = pout;
        this.instrs = instrs;
        this.myInput = myInput;
    }

    public void run() {
        while (myInput.isAlive() || hasPerson()) {
            // sleep
            if (!hasPerson()) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // work
            try {
                // take an instruction change the state of elevator
                synchronized (this) {
                    if (!instrs.isEmpty()) {
                        Instruction instr = instrs.take();
                        executeInstuction(instr);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void getOnFloorPerson() {
        Iterator<Person> iter = pout.iterator();
        while (iter.hasNext()) {
            Person p = iter.next();
            if (p.getStart() == floor) {
                iter.remove();
                pin.add(p);
                TimableOutput.println("IN-" + p.getId() + "-" + floor);
            }
        }
    }

    public void putOffFloorPerson() {
        Iterator<Person> iter = pin.iterator();
        while (iter.hasNext()) {
            Person p = iter.next();
            if (p.getTarget() == floor) {
                iter.remove();
                TimableOutput.println("OUT-" + p.getId() + "-" + floor);
            }
        }
    }

    public synchronized void up() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            //System.out.println("Elevator up is interrupted!");
            e.printStackTrace();
        }
        floor++;
        TimableOutput.println("ARRIVE-" + floor);
    }

    public synchronized void down() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            //System.out.println("Elevator down is interrupted!");
            e.printStackTrace();
        }
        floor--;
        TimableOutput.println("ARRIVE-" + floor);
    }

    public void openClose() {
        state = ElevatorState.OPEN;
        TimableOutput.println("OPEN-" + floor);
        // people get in and get off
        putOffFloorPerson();
        try {
            sleep(200);
        } catch (InterruptedException e) {
            //System.out.println("Elevator open is interrupted!");
            e.printStackTrace();
        }

        try {
            sleep(200);
        } catch (InterruptedException e) {
            //System.out.println("Elevator close is interrupted!");
            e.printStackTrace();
        }
        getOnFloorPerson();
        state = ElevatorState.RUN;
        TimableOutput.println("CLOSE-" + floor);

    }

    public int getFloor() {
        return floor;
    }

    public ElevatorState getEleState() {
        return state;
    }

    public boolean hasPerson() {
        return !pin.isEmpty() || !pout.isEmpty();
    }

    public void executeInstuction(Instruction instr) {
        switch (instr) {
            case UP:
                up();
                break;
            case DOWN:
                down();
                break;
            case OPENCLOSE:
                openClose();
                break;

            default:
                System.out.println("Error Instruction");
        }
    }
}


