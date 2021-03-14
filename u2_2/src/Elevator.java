import com.oocourse.TimableOutput;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator extends Thread {
    private char id;
    private  Controller controller;
    private int floor = 1;
    private int maxfloor = -4;
    private int minfloor = 17;
    private int initFloor;
    private int capacity = 7;
    // 请求队列pout
    private LinkedBlockingQueue<Person> pout;
    private LinkedBlockingQueue<Person> pin;
    private boolean up = true;

    public Elevator(int id,
                    int initFloor) {
        this.id = (char)('A' + id);
        this.initFloor = initFloor;
        this.pin = new LinkedBlockingQueue<>();
        this.pout = new LinkedBlockingQueue<>();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void run() {
        while (true) {
            if (!keepAlive()) { break; }
            // take an instruction
            Instruction instr = null;
            synchronized (controller) {
                //TimableOutput.println(getElevName() + " has lock controller");
                instr = look();
                //TimableOutput.println(getElevName() + " release lock controller");
            }
            // execute the instruction
            if (instr != null) {
                //TimableOutput.println(getElevName() + " : before exe");
                executeInstuction(instr);
                //TimableOutput.println(getElevName() + " : After exe");
            } else {
                try {
                    //TimableOutput.println(getElevName() + " : before sleep");
                    sleep(100);
                    //TimableOutput.println(getElevName() + " : After sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Instruction look() {
        // no pin and no pout
        // System.out.println(getElevName() + " has num_pout " + pout.size());
        if (!hasPerson()) {
            // use the default floor for every elevator
            //if (floor > initFloor) { return Instruction.DOWN; }
            //if (floor < initFloor) { return Instruction.UP; }
            return null;
        }
        boolean[] infloors = new boolean[19];  // 0 - 18
        boolean[] outfloors = new boolean[19];
        // init array
        for (int i = 0; i < infloors.length; i++) {
            // 有人进和有人出的楼层
            infloors[i] = false;
            outfloors[i] = false;
        }
        maxfloor = -4;
        minfloor = 17;
        // open and close for  inpeople
        for (Person p : pin) {
            // 对于pin,只用关心outfloor
            //int start = p.getStart();
            int target = p.getTarget();
            outfloors[F2I(p.getTarget())] = true;
            //maxfloor = getMax(start, target, maxfloor);
            //minfloor = getMin(start, target, minfloor);
            maxfloor = Math.max(target, maxfloor);
            minfloor = Math.min(target, minfloor);
        }
        // open and close for  outpeople
        for (Person p : pout) {
            // 对于pout，只用关心进
            int start = p.getStart();
            //int target = p.getTarget();
            infloors[F2I(p.getStart())] = true;
            //maxfloor = getMax(start, target, maxfloor);
            //minfloor = getMin(start, target, minfloor);
            maxfloor = Math.max(start, maxfloor);
            minfloor = Math.min(start, minfloor);
        }
        // someone get on or get off
        // 当有进电梯的人的时候，只要有空间才开门；
        // 又要出电梯的人，一定开门
        if ((infloors[F2I(floor)] && hasSpace()) || outfloors[F2I(floor)]) {
            return Instruction.OPENCLOSE;
        }

        if (floor >= maxfloor) { up = false; }
        else if (floor <= minfloor) { up = true; }
        // up
        //if (floor < maxfloor && up) {
        if (up) {
            return Instruction.UP;
        }
        // down
        //if (floor > minfloor && !up) {
        return Instruction.DOWN;
    }

    /*
    private int getMax(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    private int getMin(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }*/

    // floor to index
    private int F2I(int floor) {
        assert (floor >= -3 && floor <= -1)
                || (floor >= 1 && floor <= 16) :
                "floor out of range";
        if (floor < 0) { return floor + 3; }
        return floor + 2;
    }

    public LinkedBlockingQueue<Person> getPin() {
        return pin;
    }

    public LinkedBlockingQueue<Person> getPout() {
        return pout;
    }

    public char getElevName() {
        return id;
    }

    public void addPout(Person p) {
        // only be used in Controller
        synchronized (controller) {
            pout.add(p);
            //System.out.println(getElevName() + " pout has " + p.getId());
        }
    }

    public void getOnFloorPerson() {
        synchronized (controller) {
            Iterator<Person> iter = pout.iterator();
            while (iter.hasNext()) {
                Person p = iter.next();
                if (!hasSpace()) {
                    break;
                }
                if (p.getStart() == floor) {
                    iter.remove();
                    pin.add(p);
                    TimableOutput.println("IN-" + p.getId() + "-" + floor + "-" + id);
                }
            }
        }
    }

    public  void putOffFloorPerson() {
        synchronized (controller) {
            Iterator<Person> iter = pin.iterator();
            while (iter.hasNext()) {
                Person p = iter.next();
                if (p.getTarget() == floor) {
                    iter.remove();
                    TimableOutput.println("OUT-" + p.getId() + "-" + floor + "-" + id);
                }
            }
        }
    }

    public void up() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            //System.out.println("Elevator up is interrupted!");
            e.printStackTrace();
        }
        if (floor != -1) { floor++; }
        else { floor = 1; }
        TimableOutput.println("ARRIVE-" + floor + "-" + id);
    }

    public void down() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            //System.out.println("Elevator down is interrupted!");
            e.printStackTrace();
        }
        if (floor != 1) { floor--; }
        else { floor = -1; }
        TimableOutput.println("ARRIVE-" + floor + "-" + id);
    }

    public void openClose() {
        TimableOutput.println("OPEN-" + floor + "-" + id);
        // people get in and get off
        putOffFloorPerson();
        //TimableOutput.println(getElevName() + " After put off");
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
        //TimableOutput.println(getElevName() + " Before put on");
        getOnFloorPerson();
        TimableOutput.println("CLOSE-" + floor + "-" + id);

    }

    public int getFloor() {
        return floor;
    }

    public int getMaxfloor() {
        return maxfloor;
    }

    public int getMinfloor() {
        return minfloor;
    }

    public boolean Up() {
        return up;
    }

    public boolean hasPerson() {
        synchronized (controller) {
            return !pin.isEmpty() || !pout.isEmpty();
        }
    }

    public boolean keepAlive() {
        synchronized (controller) {
            return controller.isAlive() || hasPerson();
        }
    }

    public boolean hasSpace() {
        synchronized (controller) {
            return pin.size() < capacity;
        }
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


