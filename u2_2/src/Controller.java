import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller extends Thread {
    private final LinkedBlockingQueue<Person> pout;
    private ArrayList<Elevator> arrElevator;
    private Input myInput;

    public Controller(LinkedBlockingQueue<Person> pout,
                      ArrayList<Elevator> arrElevator,
                      Input myInput) {
        this.pout = pout;
        this.arrElevator = arrElevator;
        this.myInput = myInput;
    }

    public void run() {
        while (true) {
            // end the controller
            if (!keepAlive()) {
                break;
            }
            // lock for elevator's pout queue

            boolean sleepCont = false;
            synchronized (pout) {
                //TimableOutput.println("Controller has lock pout");
                synchronized (this) {
                    //TimableOutput.println("Controller has lock controller");
                    if (!hasPerson()) {
                        //TimableOutput.println("Controller enter sleep branch");
                        sleepCont = true;
                        //TimableOutput.println("Controller exit sleep branch");
                    } else {
                        //TimableOutput.println("Controller enter assign branch");
                        assign();
                        //TimableOutput.println("Controller exit assign branch");
                    }
                    //TimableOutput.println("Controller release lock controller");
                    // work
                }
                //TimableOutput.println("Controller release lock pout");
            }
            // sleep
            if (sleepCont) {
                try {
                    //TimableOutput.println("Controller : before sleep");
                    sleep(100);
                    //TimableOutput.println("Controller : After sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void assign() {
        // assign people to elevator's pout
        Iterator<Person> iter = pout.iterator();
        while (iter.hasNext()) {
            Person p = iter.next();
            //System.out.println(p.getId());
            iter.remove();
            boolean isAssigned = false;
            for (Elevator e : arrElevator) {
                if (sameDirect(e, p) && hasSapce(e)) {
                    // System.out.println(p.getId() + " on " + e.getElevName());
                    e.addPout(p);
                    isAssigned = true;
                    //TimableOutput.println(p.getId() + " enter " + e.getElevName());
                    break;
                }
            }
            if (!isAssigned) {
                // 分配给距离最近的
                int[] dis = new int[arrElevator.size()];
                for (int i = 0; i < arrElevator.size(); i++) {
                    Elevator e = arrElevator.get(i);
                    //pnum[i] = e.getPin().size() + e.getPout().size();
                    if (e.Up()) {
                        dis[i] = Math.abs(e.getMaxfloor() - p.getStart()) +
                                Math.abs(e.getMaxfloor() - e.getFloor());
                    } else {
                        dis[i] = Math.abs(e.getMinfloor() - p.getStart()) +
                                Math.abs(e.getMinfloor() - e.getFloor());
                    }
                }
                int dismin = 10000;
                int imin = -1;
                for (int i = dis.length - 1; i >= 0; i--) {
                    if (dis[i] < dismin) {
                        dismin = dis[i];
                        imin = i;
                    }
                }
                assert imin >= 0;
                arrElevator.get(imin).addPout(p);
                //TimableOutput.println(p.getId() +
                // " enter " + arrElevator.get(imin).getElevName());
                // System.out.println(p.getId() + " on " + arrElevator.get(imin).getElevName());
            }
        }

    }

    private boolean hasSapce(Elevator e) {
        return e.getPin().size() + e.getPout().size() < 10;
    }

    private boolean sameDirect(Elevator e, Person p) {
        if (e.Up() && p.getStart() < p.getTarget() && e.getFloor() <= p.getStart()) {
            return true;
        }
        if (!e.Up() && p.getStart() > p.getTarget() && e.getFloor() >= p.getStart()) {
            return true;
        }
        return false;
    }

    public synchronized boolean hasPerson() {
        return !pout.isEmpty();
    }

    public synchronized boolean keepAlive() {
        return hasPerson() || myInput.isAlive();
    }

}
