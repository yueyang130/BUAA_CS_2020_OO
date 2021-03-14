import com.oocourse.elevator3.PersonRequest;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator extends Thread {
    private String id;
    private String type;
    private boolean[] range = new boolean[23];
    private double moveTime;
    private int capacity;
    private final Controller controller;

    private int floor = 1;
    private int maxfloor = -4;
    private int minfloor = 21;
    private int initFloor;

    // 请求队列pout
    private LinkedBlockingQueue<PersonRequest> pout;
    private LinkedBlockingQueue<PersonRequest> pin;
    private boolean up = true;

    public Elevator(int id, Controller controller, int initFloor) {
        this(String.valueOf((char)('A' + id)),
                String.valueOf((char)('A' + id)), controller, initFloor);
    }

    public Elevator(int id, Controller controller) {
        this(String.valueOf((char)('A' + id)),
                String.valueOf((char)('A' + id)), controller, 1);
    }

    public Elevator(String id, String type, Controller controller) {
        this(id, type, controller, 1);
    }

    public Elevator(String id, String type, Controller controller, int initFloor) {
        this.id = id;
        this.type = type;
        this.initFloor = initFloor;
        this.controller = controller;
        this.pin = new LinkedBlockingQueue<>();
        this.pout = new LinkedBlockingQueue<>();
        assert type.equals("A") || type.equals("B") || type.equals("C");
        if (type.equals("A")) {
            for (int i = 0; i < range.length; i++) {
                range[i] = I2F(i) >= -3 && I2F(i) <= -1 || I2F(i) == 1
                        || I2F(i) >= 15 && I2F(i) <= 20;
            }
            moveTime = 0.4;
            capacity = 6;
        } else if (type.equals("B")) {
            for (int i = 0; i < range.length; i++) {
                range[i] = I2F(i) != 0 && I2F(i) >= -2 && I2F(i) <= 2 ||
                        I2F(i) >= 4 && I2F(i) <= 15;
            }
            moveTime = 0.5;
            capacity = 8;
        } else {
            for (int i = 0; i < range.length; i++) {
                range[i] = I2F(i) >= 1 && I2F(i) <= 15 && I2F(i) % 2 == 1;
            }
            moveTime = 0.6;
            capacity = 7;
        }
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
                // 在execute方法里，要改变pin.pout的地方，需要加controller锁
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
        if (!hasPersonRequest()) {
            // use the init floor for every elevator
            if (floor > initFloor) { return Instruction.DOWN; }
            if (floor < initFloor) { return Instruction.UP; }
            return null;
        }
        boolean[] infloors = new boolean[23];  // 0 - 22
        boolean[] outfloors = new boolean[23];
        // init array
        for (int i = 0; i < infloors.length; i++) {
            // 有人进和有人出的楼层
            infloors[i] = false;
            outfloors[i] = false;
        }
        maxfloor = -4;
        minfloor = 21;
        // open and close for  inpeople
        for (PersonRequest p : pin) {
            // 对于pin,只用关心outfloor
            //int target = p.getToFloor();
            int target = getSwitchFloor(p);
            outfloors[F2I(target)] = true;
            maxfloor = Math.max(target, maxfloor);
            minfloor = Math.min(target, minfloor);
        }
        // open and close for  outpeople
        for (PersonRequest p : pout) {
            // 对于pout，只用关心进
            int start = p.getFromFloor();
            infloors[F2I(p.getFromFloor())] = true;
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

    private int getSwitchFloor(PersonRequest p) {
        if (range[F2I(p.getToFloor())]) {
            return p.getToFloor();
        }
        if (type.equals("A")) {
            if (p.getFromFloor() < p.getToFloor()) {
                return 1;
            } else {
                return 15;
            }
        }
        if (type.equals("B")) {
            // 只关注p.ToFloor不在typeB Elevator范围内的
            // 返回值必须在typeB Elevator范围内且在其他种类的电梯范围内
            if (p.getToFloor() == -3) {
                return  -2;
            } else if (p.getToFloor() >= 16) {
                return 15;
            } else if (p.getToFloor() == 3 && p.getFromFloor() < p.getToFloor()) {
                return 1;
            } else if (p.getToFloor() == 3 && p.getFromFloor() > p.getToFloor()) {
                return 5;
            }
            SafeOutput.println("in class Elevator, function getSwitchFloor, type B, Error!");
        }
        if (p.getToFloor() >= 16) { return 15; }
        if (p.getToFloor() <= -1) { return 1; }

        assert p.getToFloor() >= 2 && p.getToFloor() <= 14 && p.getToFloor() % 2 == 0;
        if (p.getFromFloor() < p.getToFloor()) {
            if (p.getToFloor() == 4) {
                if (p.getFromFloor() == 1) {
                    return 1;
                }  // 3 to 4
                return 5;
            }  // 2,6,8...,14
            else { return p.getToFloor() - 1; }
        } else {
            if (p.getToFloor() == 2) {
                if (p.getFromFloor() == 3) {  // 3 to 2
                    return 1;
                }
                return 5; // 5,7,9... to 2
            }
            else { return p.getToFloor() + 1; } // to 4,6,8...,14
        }
        /*if (p.getToFloor() >= 4) { // 2,4,6,...,14
            assert p.getToFloor() >= 4 && p.getToFloor() <= 14 && p.getToFloor() % 2 == 0;
            if (p.getFromFloor() < p.getToFloor()) {
                return p.getToFloor() - 1;
            } else { return p.getToFloor() + 1; }
        }
        return 1; // when toFloor == 2, 4*/
    }

    // floor to index
    private int F2I(int floor) {
        assert (floor >= -3 && floor <= -1)
                || (floor >= 1 && floor <= 16) :
                "floor out of range";
        if (floor < 0) { return floor + 3; }
        return floor + 2;
    }

    private int I2F(int index) {
        assert index >= 0 && index <= 22 : "index out of range";
        if (index <= 2) { return index - 3; }
        return index - 2;
    }

    public boolean arriveDirectly(PersonRequest p) {
        return range[F2I(p.getFromFloor())] && range[F2I(p.getToFloor())];
    }

    public LinkedBlockingQueue<PersonRequest> getPin() {
        return pin;
    }

    public LinkedBlockingQueue<PersonRequest> getPout() {
        return pout;
    }

    public double getMoveTime() {
        return moveTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getElevName() {
        return id;
    }

    public void addPout(PersonRequest p) {
        // only be used in Controller
        synchronized (controller) {
            pout.add(p);
            //System.out.println(getElevName() + " pout has " + p.getId());
        }
    }

    public void getOnFloorPersonRequest() {
        synchronized (controller) {
            Iterator<PersonRequest> iter = pout.iterator();
            while (iter.hasNext()) {
                PersonRequest p = iter.next();
                if (!hasSpace()) {
                    break;
                }
                if (p.getFromFloor() == floor) {
                    iter.remove();
                    pin.add(p);
                    SafeOutput.println("IN-" + p.getPersonId() + "-" + floor + "-" + id);
                }
            }
        }
    }

    public  void putOffFloorPersonRequest() {
        synchronized (controller) {
            Iterator<PersonRequest> iter = pin.iterator();
            while (iter.hasNext()) {
                PersonRequest p = iter.next();
                // if (p.getToFloor() == floor) {
                if (getSwitchFloor(p) == floor) {
                    iter.remove();
                    SafeOutput.println("OUT-" + p.getPersonId() + "-" + floor + "-" + id);
                    feedback(p, floor);
                }
            }
        }
    }

    private void feedback(PersonRequest p, int floor) {
        if (p.getToFloor() == floor) { return; }
        PersonRequest newp = new PersonRequest(floor, p.getToFloor(), p.getPersonId());
        controller.put(newp);
    }

    public void up() {
        try {
            sleep((long)(1000 * moveTime));
        } catch (InterruptedException e) {
            //System.out.println("Elevator up is interrupted!");
            e.printStackTrace();
        }
        synchronized (controller) {
            if (floor != -1) { floor++; }
            else { floor = 1; }
            SafeOutput.println("ARRIVE-" + floor + "-" + id);
        }
    }

    public void down() {
        try {
            sleep((long)(1000 * moveTime));
        } catch (InterruptedException e) {
            //System.out.println("Elevator down is interrupted!");
            e.printStackTrace();
        }
        synchronized (controller) {
            if (floor != 1) { floor--; }
            else { floor = -1; }
            SafeOutput.println("ARRIVE-" + floor + "-" + id);
        }

    }

    public void openClose() {

        SafeOutput.println("OPEN-" + floor + "-" + id);

        // people get in and get off
        putOffFloorPersonRequest();
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
        getOnFloorPersonRequest();

        SafeOutput.println("CLOSE-" + floor + "-" + id);


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

    public boolean hasPersonRequest() {
        synchronized (controller) {
            return !pin.isEmpty() || !pout.isEmpty();
        }
    }

    public boolean keepAlive() {
        synchronized (controller) {
            return controller.isAlive() || hasPersonRequest();
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


