import com.oocourse.TimableOutput;

public class SafeOutput {
    public static synchronized void println(String str) {
        TimableOutput.println(str);
    }
}
