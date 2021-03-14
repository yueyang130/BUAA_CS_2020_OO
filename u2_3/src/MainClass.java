import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args)  {
        // put initStamp at the head of the code
        TimableOutput.initStartTimestamp();

        Controller controller = new Controller();
        Input myInput = new Input(controller);
        controller.setInput(myInput);
        myInput.start();
        controller.start();
        controller.startElevators();
    }

}
