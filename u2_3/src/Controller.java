import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller extends Thread {
    private final LinkedBlockingQueue<Request> requests;
    private final Vector<Elevator> arrElevator;
    private final Vector<Elevator> arrA;
    private final Vector<Elevator> arrB;
    private final Vector<Elevator> arrC;
    // At most, we have 4 type A elevator
    private final int[] initA = {15, -3, 15, -3};
    private final int[] initB = {1, 5, 15, -2};
    private final int[] initC = {1, 5, 15, 1};
    private int posInitA;
    private int posInitB;
    private int posInitC;

    private  Input myInput;

    public Controller() {
        this.requests = new LinkedBlockingQueue<>();
        this.arrElevator = new Vector<>();
        this.arrA = new Vector<>();
        this.arrB = new Vector<>();
        this.arrC = new Vector<>();

        arrElevator.add(new Elevator(0, this, initA[posInitA++]));
        arrElevator.add(new Elevator(1, this, initB[posInitB++]));
        arrElevator.add(new Elevator(2, this, initC[posInitC++]));

        arrA.add(arrElevator.get(0));
        arrB.add(arrElevator.get(1));
        arrC.add(arrElevator.get(2));
    }

    public void setInput(Input myInput) {
        this.myInput = myInput;
    }

    public synchronized void startElevators() {
        for (Elevator elevator : arrElevator) {
            elevator.start();
        }
    }

    public synchronized void put(Request request) {
        requests.add(request);
    }

    public void run() {
        while (true) {
            // end the controller
            synchronized (this) {
                if (!keepAlive()) {
                    break;
                }
            }
            // unit2 homework2, controller class, run method
            // lock for elevator's pout queue
            boolean sleepCont = false; // 设立标志位， 作为是否要sleep的标志
            synchronized (this) {
                if (!hasRequest()) {
                    sleepCont = true;  // let contoller sleep
                } else {
                    take();
                }

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

    private void makeElevator(Request r) {
        ElevatorRequest er = (ElevatorRequest) r;
        String type = er.getElevatorType();
        int init;
        if (type.equals("A")) {
            init = initA[posInitA++];
        }
        else if (type.equals("B")) {
            init = initB[posInitB++];
        }
        else {
            init = initC[posInitC++];
        }
        Elevator elevator = new Elevator(er.getElevatorId(),
                er.getElevatorType(), this, init);
        elevator.start();
        arrElevator.add(elevator);
        if (type.equals("A")) {
            arrA.add(elevator);
        }
        else if (type.equals("B")) {
            arrB.add(elevator);
        }
        else {
            arrC.add(elevator);
        }
    }

    public void take() {
        // 取出request
        Iterator<Request> iter = requests.iterator();
        while (iter.hasNext()) {
            Request r = iter.next();
            iter.remove();
            // add an elevator thread
            if (r instanceof ElevatorRequest) {
                makeElevator(r);
                continue;
            }
            // assign people to elevator's pout
            PersonRequest p = (PersonRequest) r;
            // start floor can be only reached by one type elevator
            int start = p.getFromFloor();
            if (start == -3 || start >= 16 && start <= 20) {
                assign(arrA, p);
                return;
            }
            if (start >= 2 && start <= 14 && start % 2 == 0) {
                assign(arrB, p);
                return;
            }
            if (start == 3) {
                assign(arrC, p);
                return;
            }
            // start floor can be reached for two type elevator
            if (start == -2 || start == -1) {
                Vector<Elevator> ab = new Vector<>();
                ab.addAll(arrA);
                ab.addAll(arrB);
                assign(ab, p);
                return;
            }
            if (start >= 5 && start <= 13 && start % 2 == 1) {
                Vector<Elevator> bc = new Vector<>();
                bc.addAll(arrB);
                bc.addAll(arrC);
                assign(bc, p);
                return;
            }
            if (start == 1 || start == 15) {
                assign(arrElevator, p);
                return;
            }
        }
    }

    private void assign(Vector<Elevator> arr, PersonRequest p) {
        //  先分配给可直达
        //  再分配给可搭载的
        // 如果没有， 则分配给总用时最少的
        // 衡量标准： 时间 * 距离
        for (Elevator e : arr) {
            if (e.arriveDirectly(p)) {
                e.addPout(p);
                return;
            }
        }

        for (Elevator e : arr) {
            if (sameDirect(e, p) && hasSapce(e)) {
                e.addPout(p);
                return;
            }
        }

        // 分配给距离 * 单位用时最小的
        double[] dis = new double[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            Elevator e = arr.get(i);
            //pnum[i] = e.getPin().size() + e.getPout().size();
            if (e.Up()) {
                dis[i] = (Math.abs(e.getMaxfloor() - p.getFromFloor()) +
                        Math.abs(e.getMaxfloor() - e.getFloor())) * e.getMoveTime();
            } else {
                dis[i] = (Math.abs(e.getMinfloor() - p.getFromFloor()) +
                        Math.abs(e.getMinfloor() - e.getFloor())) * e.getMoveTime();
            }
        }
        double dismin = 10000;
        int imin = -1;
        for (int i = dis.length - 1; i >= 0; i--) {
            if (dis[i] < dismin) {
                dismin = dis[i];
                imin = i;
            }
        }
        assert imin >= 0;
        arr.get(imin).addPout(p);

    }

    private boolean hasSapce(Elevator e) {
        return e.getPin().size() + e.getPout().size() < e.getCapacity() + 3;
    }

    private boolean sameDirect(Elevator e, PersonRequest p) {
        if (e.Up() && p.getFromFloor() < p.getToFloor() && e.getFloor() <= p.getFromFloor()) {
            return true;
        }
        if (!e.Up() && p.getFromFloor() > p.getToFloor() && e.getFloor() >= p.getFromFloor()) {
            return true;
        }
        return false;
    }

    public synchronized boolean hasRequest() {
        return !requests.isEmpty();
    }

    public synchronized boolean keepAlive() {
        boolean keepAlive = hasRequest() || myInput.isAlive();
        for (Elevator e : arrElevator) {
            keepAlive = keepAlive || e.hasPersonRequest();
        }
        return keepAlive;
    }

}
