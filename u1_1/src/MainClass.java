import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args)  {
        Scanner in;
        in = new Scanner(System.in);
        while (in.hasNext()) {
            String s = in.nextLine();
            PolyComputer cp = new PolyComputer();
            cp.parsePoly(s);
            cp.printdPoly();
        }
    }
}
