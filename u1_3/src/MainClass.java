import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            Parser myParser = new Parser();
            String line = in.nextLine();
            boolean tf = myParser.ExpJudger(line);  // true format
            if (tf) {
                try {
                    Exp myExp = myParser.expParser(line);
                    myExp.simplify();
                    //System.out.println(myExp.toString());
                    Exp dexp = myExp.diff();
                    dexp.simplify();
                    System.out.println(dexp.toString());
                    //myExp.simplify();
                    //System.out.println(myExp.toString());
                } catch (IndexException e) {
                    System.out.println("WRONG FORMAT!");
                }

            }
            else { System.out.println("WRONG FORMAT!"); }
        }
    }

}
