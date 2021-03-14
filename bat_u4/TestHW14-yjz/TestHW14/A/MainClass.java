
import com.oocourse.uml2.interact.AppRunner;
import com.oocourse.uml2.general.MyUmlGeneralInteraction;

public class MainClass {
    public static void main(String[] args) throws Exception {
        AppRunner appRunner = AppRunner.newInstance(MyUmlGeneralInteraction.class);
        appRunner.run(args);
    }
}
