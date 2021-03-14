import com.oocourse.TimableOutput;

import java.util.concurrent.LinkedBlockingQueue;

public class Controller extends Thread {
    private LinkedBlockingQueue<Person> pin;
    private LinkedBlockingQueue<Person> pout;
    private  LinkedBlockingQueue<Instruction> instrs;
    private Elevator elevator;
    private Input myInput;
    private boolean up = true;

    public Controller(LinkedBlockingQueue<Person> pin,
                      LinkedBlockingQueue<Person> pout,
                      LinkedBlockingQueue<Instruction> instrs,
                      Elevator elevator,
                      Input myInput) {
        this.pin = pin;
        this.pout = pout;
        this.instrs  = instrs;
        this.elevator = elevator;
        this.myInput = myInput;
    }

    public void run() {
        while (true) {
            // end the controller
            if (!myInput.isAlive() && !hasPerson()) {
                break;
            }
            // sleep
            // 用elevator做监控器的理由是：
            // hasPerson和look方法的核心都是pin和pout, instr对象，
            // 他们都是elevator的数据
            synchronized (elevator) {
                if (!hasPerson()) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    look();
                }
                // work
            }
        }
    }

    public void look() {
        instrs.clear();
        boolean[] upfloors = new boolean[16];  // 0 - 15
        boolean[] downfloors = new boolean[16];
        // init array
        for (int i = 0; i < upfloors.length; i++) {
            upfloors[i] = false;
            downfloors[i] = false;
        }
        int maxfloor = 0;
        int minfloor = 16;
        // open and close for  inpeople
        for (Person p : pin) {
            int start = p.getStart();
            int target = p.getTarget();
            if (start < target) {
                upfloors[p.getTarget()] = true;
                maxfloor = Math.max(target, maxfloor);
                minfloor = Math.min(start, minfloor);
            }
            if (start >= target) {
                downfloors[p.getTarget()] = true;
                maxfloor = Math.max(start, maxfloor);
                minfloor = Math.min(target, minfloor);
            }

        }
        // open and close for  outpeople
        for (Person p : pout) {
            int start = p.getStart();
            int target = p.getTarget();
            if (start < target) {
                upfloors[p.getStart()] = true;
                maxfloor = Math.max(target, maxfloor);
                minfloor = Math.min(start, minfloor);
            }
            if (start >= target) {
                downfloors[p.getStart()] = true;
                maxfloor = Math.max(start, maxfloor);
                minfloor = Math.min(target, minfloor);
            }
        }
        // someone get on or get off
        int floor = elevator.getFloor();

        if (upfloors[floor] || downfloors[floor]) {
            instrs.add(Instruction.OPENCLOSE);
            return;
        }

        if (floor >= maxfloor) { up = false; }
        else if (floor <= minfloor) { up = true; }
        // up
        //if (floor < maxfloor && up) {
        if (up) {
            instrs.add(Instruction.UP);
            return;
        }
        // down
        //if (floor > minfloor && !up) {
        if (!up) {
            instrs.add(Instruction.DOWN);
            return;
        }
    }

    public boolean hasPerson() {
        return !pin.isEmpty() || !pout.isEmpty();
    }

}
